package com.grahambartley.volley.gametest;

import com.google.gson.JsonObject;
import com.grahambartley.volley.ModArrows;
import com.grahambartley.volley.arrow.ArrowDefinition;
import com.grahambartley.volley.arrow.ArrowTextureAudit;
import com.grahambartley.volley.arrow.ArrowTextures;
import com.grahambartley.volley.arrow.TextureDigest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;

public final class ArrowTextureAuditGameTest implements FabricGameTest {
  private static final String BATCH = "release-audit-textures";
  private static final String PARTICLE = "particle";

  @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, batchId = BATCH, tickLimit = 10)
  public void everyArrowHasAnItemSprite(TestContext context) {
    final List<Identifier> missing = ArrowTextureAudit.missing(itemSprites());

    context.assertTrue(missing.isEmpty(), "Arrows with no item sprite (REL-18): " + missing);
    context.complete();
  }

  @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, batchId = BATCH, tickLimit = 10)
  public void noTwoArrowsShareAnItemSprite(TestContext context) {
    final List<Identifier> shared = ArrowTextureAudit.shared(itemSprites());

    context.assertTrue(shared.isEmpty(), "Arrows sharing an item sprite (REL-18): " + shared);
    context.complete();
  }

  private static Map<Identifier, Optional<String>> itemSprites() {
    final Map<Identifier, Optional<String>> digests = new LinkedHashMap<>();
    for (final ArrowDefinition<?> definition : ModArrows.catalog().definitions()) {
      digests.put(definition.id(), itemSpriteLayers(definition.id()).map(TextureDigest::of));
    }
    return digests;
  }

  private static Optional<List<byte[]>> itemSpriteLayers(final Identifier arrowId) {
    final Optional<JsonObject> model =
        ModAssetsSupport.json(
            Identifier.of(arrowId.getNamespace(), "models/item/" + arrowId.getPath() + ".json"));
    if (model.isEmpty() || !model.get().has("textures")) {
      return Optional.empty();
    }
    final JsonObject textures = model.get().getAsJsonObject("textures");
    final String sharedParts = ArrowTextures.itemParts(arrowId.getNamespace()).toString();
    final List<byte[]> layers = new ArrayList<>();
    for (final String key : new TreeSet<>(textures.keySet())) {
      final String texture = textures.get(key).getAsString();
      if (key.equals(PARTICLE) || texture.equals(sharedParts)) {
        continue;
      }
      final Optional<byte[]> png = ModAssetsSupport.texture(Identifier.of(texture));
      if (png.isEmpty()) {
        return Optional.empty();
      }
      layers.add(png.get());
    }
    return layers.isEmpty() ? Optional.empty() : Optional.of(layers);
  }
}
