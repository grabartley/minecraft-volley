package com.grahambartley.volley.arrow;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.util.Identifier;

public final class ArrowAura {
  public static final int DEFAULT_COLOR = 0xE2D6BE;

  private static final Map<String, Integer> COLORS =
      Map.ofEntries(
          Map.entry("allegiance_arrow", 0xE8B923),
          Map.entry("beacon_arrow", 0xFFC940),
          Map.entry("bee_arrow", 0xE3A826),
          Map.entry("blossom_arrow", 0xF7D23E),
          Map.entry("boomerang_arrow", 0x896727),
          Map.entry("bridge_arrow", 0xB08A50),
          Map.entry("chicken_arrow", 0xE8302A),
          Map.entry("courier_arrow", 0x2E9C96),
          Map.entry("disarm_arrow", 0xD8322A),
          Map.entry("drain_arrow", 0xD2C646),
          Map.entry("drill_arrow", 0x896727),
          Map.entry("ender_pearl_arrow", 0x2FB894),
          Map.entry("fire_charge_arrow", 0xFFC060),
          Map.entry("freeze_arrow", 0x7FAAF3),
          Map.entry("frost_arrow", 0xBFE8FF),
          Map.entry("glow_ink_arrow", 0x8AF7DE),
          Map.entry("grapple_arrow", 0xB4C1CE),
          Map.entry("gravity_arrow", 0x76C84F),
          Map.entry("guard_arrow", 0xC8A064),
          Map.entry("gunpowder_arrow", 0x896727),
          Map.entry("harvest_arrow", 0x896727),
          Map.entry("haste_arrow", 0xF3D34A),
          Map.entry("homing_arrow", 0xF03A3A),
          Map.entry("incendiary_arrow", 0xFFD04A),
          Map.entry("levitation_arrow", 0xC07CE0),
          Map.entry("lifesteal_arrow", 0xD42424),
          Map.entry("magnet_arrow", 0xC81E1E),
          Map.entry("milk_arrow", 0x896727),
          Map.entry("paint_arrow", 0x896727),
          Map.entry("party_arrow", 0xFFF6A0),
          Map.entry("pillar_arrow", 0xA07A54),
          Map.entry("polymorph_arrow", 0xF4A8A8),
          Map.entry("prospector_arrow", 0xC890F0),
          Map.entry("puffer_arrow", 0xF0C838),
          Map.entry("railgun_arrow", 0x6FD8FF),
          Map.entry("recall_arrow", 0x9A4FE0),
          Map.entry("redstone_arrow", 0xD71414),
          Map.entry("repel_arrow", 0xA07A5C),
          Map.entry("ricochet_arrow", 0x896727),
          Map.entry("rope_arrow", 0xD8C69B),
          Map.entry("rust_arrow", 0xB8662E),
          Map.entry("sapling_arrow", 0x6FBF4A),
          Map.entry("scaffold_arrow", 0xD8B964),
          Map.entry("shear_arrow", 0x896727),
          Map.entry("shock_arrow", 0xFFF27A),
          Map.entry("smoke_arrow", 0xFFC060),
          Map.entry("snow_golem_arrow", 0xE8901A),
          Map.entry("sonar_arrow", 0xA8F0F0),
          Map.entry("stink_arrow", 0xD8DC8C),
          Map.entry("taunt_arrow", 0xFFF2B0),
          Map.entry("till_arrow", 0x4A90E0),
          Map.entry("tnt_arrow", 0xDB3B28),
          Map.entry("torch_arrow", 0xFFD04A),
          Map.entry("tow_arrow", 0xA8404F),
          Map.entry("tracer_arrow", 0x8AF7DE),
          Map.entry("trampoline_arrow", 0x5FCF46),
          Map.entry("tripwire_arrow", 0x2FA9C0),
          Map.entry("updraft_arrow", 0xEEF3FF),
          Map.entry("vine_arrow", 0x86C94A),
          Map.entry("volley_arrow", 0x896727),
          Map.entry("web_arrow", 0x896727),
          Map.entry("wind_arrow", 0xE2F6F8),
          Map.entry("zipline_arrow", 0xC8B48A));

  private static final Set<String> GLOWING =
      Set.of(
          "beacon_arrow",
          "drain_arrow",
          "ender_pearl_arrow",
          "fire_charge_arrow",
          "freeze_arrow",
          "frost_arrow",
          "glow_ink_arrow",
          "haste_arrow",
          "homing_arrow",
          "incendiary_arrow",
          "levitation_arrow",
          "lifesteal_arrow",
          "railgun_arrow",
          "redstone_arrow",
          "shock_arrow",
          "sonar_arrow",
          "torch_arrow",
          "tracer_arrow");

  private ArrowAura() {}

  public static int color(final Identifier arrowId) {
    Objects.requireNonNull(arrowId, "arrowId");
    return COLORS.getOrDefault(arrowId.getPath(), DEFAULT_COLOR);
  }

  public static boolean glows(final Identifier arrowId) {
    Objects.requireNonNull(arrowId, "arrowId");
    return GLOWING.contains(arrowId.getPath());
  }
}
