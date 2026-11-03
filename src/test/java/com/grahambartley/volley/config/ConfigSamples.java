package com.grahambartley.volley.config;

import java.util.List;

final class ConfigSamples {
  private ConfigSamples() {}

  static VolleyConfig everyFamilyCustomised() {
    return new VolleyConfig(
        new ExplosiveArrowConfig(
            new ExplosiveTierConfig(0, 1.0f),
            new ExplosiveTierConfig(15, 12.0f),
            new ExplosiveTierConfig(199, 20.0f),
            true,
            false,
            8,
            5999,
            1.75f,
            new IncendiaryArrowConfig(7, 42, false)),
        new GrappleArrowConfig(127, 3.9f, 0.2f, false, false, 127, true),
        new UtilityArrowConfig(5999, 1199, 1, 15.5f, 7.5f),
        new PhysicsArrowConfig(8, List.of("minecraft:bedrock"), 16, false),
        new EnderArrowConfig(96, 8, true),
        new CombatArrowConfig(
            new ShockArrowConfig(31.5f, 19.5f),
            new LifestealArrowConfig(0.95f, 19.5f),
            new StatusArrowConfig(11999, 1, 6000),
            new HomingArrowConfig(0.95f, 63.5f, 179.0f),
            new VolleyArrowConfig(12, 0.95f, 44.0f, 39),
            new RailgunArrowConfig(7.5f, 1.95f)),
        new ControlArrowConfig(
            new FrostArrowConfig(1199),
            new LevitationArrowConfig(1),
            new TargetingArrowConfig(31.5f, 5999, 0.5f, 1, 63.5f),
            new AllegianceArrowConfig(5999, 31.5f),
            new SmokeArrowConfig(15.5f, 1),
            new DisarmArrowConfig(false, 15.5f)),
        TraversalArrowConfig.defaults()
            .withZipline(ZiplineArrowConfig.defaults().withMaxSpanBlocks(100)),
        TerrainArrowConfig.defaults().withDrill(DrillArrowConfig.defaults().withToolTier(0)),
        AgricultureArrowConfig.defaults().withBee(BeeArrowConfig.defaults().withCount(8)),
        DiscoveryArrowConfig.defaults()
            .withProspector(
                ProspectorArrowConfig.defaults().withBlocks(List.of("minecraft:stone"))),
        ChaosArrowConfig.defaults()
            .withPolymorph(PolymorphArrowConfig.defaults().withEnabled(false)),
        SocialArrowConfig.defaults()
            .withCourier(
                CourierArrowConfig.defaults().withUndeliverable(List.of("minecraft:shulker_box"))),
        new FletchingStationConfig(false),
        new SoundConfig(0.35f));
  }
}
