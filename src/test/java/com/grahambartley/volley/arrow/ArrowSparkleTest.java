package com.grahambartley.volley.arrow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minecraft.particle.DustParticleEffect;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ArrowSparkleTest {

  @ParameterizedTest
  @CsvSource({
    "0xFF0000, 1.0, 0.0, 0.0",
    "0x00FF00, 0.0, 1.0, 0.0",
    "0x0000FF, 0.0, 0.0, 1.0",
    "0xFFFFFF, 1.0, 1.0, 1.0",
  })
  void splitsTheColourIntoDustChannels(
      final String rgb, final float red, final float green, final float blue) {
    final DustParticleEffect dust = ArrowSparkle.dust(Integer.decode(rgb), 0.5f);

    assertEquals(red, dust.getColor().x(), 1e-6);
    assertEquals(green, dust.getColor().y(), 1e-6);
    assertEquals(blue, dust.getColor().z(), 1e-6);
    assertEquals(0.5f, dust.getScale(), 1e-6);
  }

  @ParameterizedTest
  @CsvSource({
    "false, false, 5, true",
    "true, false, 5, false",
    "false, true, 5, false",
    "false, false, 1, false",
  })
  void trailsOnlyWhileFlying(
      final boolean inGround, final boolean removed, final int age, final boolean expected) {
    assertEquals(expected, ArrowSparkle.trails(inGround, removed, age));
  }

  @ParameterizedTest
  @CsvSource({"true, 0.9, 18", "false, 0.6, 10"})
  void glowingArrowsSparkleHarder(final boolean glows, final float scale, final int count) {
    assertEquals(scale, ArrowSparkle.trailScale(glows), 1e-6);
    assertEquals(count, ArrowSparkle.burstCount(glows));
  }
}
