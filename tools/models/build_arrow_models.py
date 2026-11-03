import base64, copy, io, json, os, subprocess, sys, uuid
from PIL import Image

repo, bbdir = sys.argv[1], sys.argv[2]
assets = f"{repo}/src/main/resources/assets/volley"
item_tex = f"{assets}/textures/item"
template = json.load(open(f"{bbdir}/arrow_template.bbmodel"))
TEMPLATE_RGB = {(137, 103, 39), (40, 30, 11), (224, 224, 224), (198, 198, 198), (63, 63, 63)}
FLETCHING_TINT = 1
TINTED_HEAD_TINT = 0
DISPLAY = {
    "thirdperson_righthand": {"rotation": [0, 0, 0], "translation": [0, 3, 1], "scale": [0.55, 0.55, 0.55]},
    "firstperson_righthand": {"rotation": [0, -90, 25], "translation": [1.13, 3.2, 1.13], "scale": [0.68, 0.68, 0.68]},
    "ground": {"rotation": [0, 0, 0], "translation": [0, 2, 0], "scale": [0.5, 0.5, 0.5]},
    "head": {"rotation": [0, 180, 0], "translation": [0, 13, 7], "scale": [1, 1, 1]},
    "fixed": {"rotation": [0, 180, 0], "translation": [0, 0, 0], "scale": [1, 1, 1]},
}


def original(name):
    raw = subprocess.run(["git", "-C", repo, "show", f"origin/main:src/main/resources/assets/volley/textures/item/{name}.png"], capture_output=True).stdout
    return Image.open(io.BytesIO(raw)).convert("RGBA")


def template_pixel(orig, x, y):
    p = orig.getpixel((x, y))
    return p[3] and p[:3] in TEMPLATE_RGB and x + y >= 13 and x <= 10 and y >= 6


def runs(mask):
    out = []
    for y in range(16):
        x = 0
        while x < 16:
            if mask(x, y):
                s = x
                while x < 16 and mask(x, y):
                    x += 1
                out.append((s, x, y))
            else:
                x += 1
    return out


def head_cube(x0, x1, y, tex, tint):
    face = lambda uv: {"uv": uv, "texture": tex, **({"tint": tint} if tint is not None else {})}
    return {
        "name": f"head_{y}_{x0}", "box_uv": False, "rescale": False, "locked": False,
        "from": [x0, 15 - y, 7], "to": [x1, 16 - y, 9], "origin": [8, 8, 8], "autouv": 0, "color": 0,
        "faces": {
            "north": face([x1, y, x0, y + 1]), "south": face([x0, y, x1, y + 1]),
            "up": face([x0, y, x1, y + 1]), "down": face([x0, y, x1, y + 1]),
            "east": face([x1 - 1, y, x1, y + 1]), "west": face([x0, y, x0 + 1, y + 1]),
        },
        "type": "cube", "uuid": str(uuid.uuid4()),
    }


def data_url(path):
    return "data:image/png;base64," + base64.b64encode(open(path, "rb").read()).decode()


def texture(name, path, idx):
    return {"name": name, "id": str(idx), "namespace": "volley", "folder": "item", "width": 16, "height": 16,
            "uv_width": 16, "uv_height": 16, "render_mode": "default", "internal": True,
            "uuid": str(uuid.uuid4()), "source": data_url(path)}


def build(name, original_name):
    sprite = Image.open(f"{item_tex}/{name}.png").convert("RGBA")
    orig = original(original_name)
    head_path = f"{item_tex}/{name.replace('_loaded', '')}_head.png"
    tinted = os.path.exists(head_path) and not name.endswith("_head")
    model = copy.deepcopy(template)
    model["name"] = name
    for el in model["elements"]:
        if el["name"].startswith("fin"):
            for f in el["faces"].values():
                f["tint"] = FLETCHING_TINT
    model["textures"] = [texture("arrow_parts", f"{bbdir}/arrow_parts.png", 0), texture(name, f"{item_tex}/{name}.png", 1)]
    mask = lambda x, y: sprite.getpixel((x, y))[3] and not template_pixel(orig, x, y)
    cubes = [head_cube(a, b, y, 1, None) for a, b, y in runs(mask)]
    if tinted:
        head = Image.open(head_path).convert("RGBA")
        model["textures"].append(texture(os.path.basename(head_path)[:-4], head_path, 2))
        cubes += [head_cube(a, b, y, 2, TINTED_HEAD_TINT) for a, b, y in runs(lambda x, y: head.getpixel((x, y))[3])]
    model["elements"] += cubes
    model["outliner"] = [e["uuid"] for e in model["elements"]]
    model["display"] = DISPLAY
    json.dump(model, open(f"{bbdir}/arrows/{name}.bbmodel", "w"))
    return model


def to_java(model, name):
    tex_ids = {i: f"volley:item/{t['name']}" for i, t in enumerate(model["textures"])}
    elements = []
    for el in model["elements"]:
        faces = {}
        for side, f in el["faces"].items():
            u = f["uv"]
            if u[0] == u[2] or u[1] == u[3]:
                continue
            jf = {"uv": u, "texture": f"#{f['texture']}"}
            if f.get("tint") is not None:
                jf["tintindex"] = f["tint"]
            faces[side] = jf
        je = {"from": el["from"], "to": el["to"], "faces": faces}
        rot = el.get("rotation")
        if rot and any(rot):
            axis = "xyz"[[i for i, v in enumerate(rot) if v][0]]
            je["rotation"] = {"angle": [v for v in rot if v][0], "axis": axis, "origin": el["origin"]}
        elements.append(je)
    textures = {str(i): v for i, v in tex_ids.items()}
    textures["particle"] = tex_ids[1]
    return {"gui_light": "front", "textures": textures, "elements": elements, "display": DISPLAY}


os.makedirs(f"{bbdir}/arrows", exist_ok=True)
for f in sorted(os.listdir(f"{assets}/models/item")):
    name = f[:-5]
    path = f"{assets}/models/item/{f}"
    old = json.load(open(path))
    model = build(name, name)
    java = to_java(model, name)
    if "overrides" in old:
        java["overrides"] = old["overrides"]
    json.dump(java, open(path, "w"), indent=2)
    open(path, "a").write("\n")
print("done")
