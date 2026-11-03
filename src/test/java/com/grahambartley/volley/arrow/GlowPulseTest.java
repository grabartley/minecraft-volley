package com.grahambartley.volley.arrow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class GlowPulseTest {

  @ParameterizedTest
  @CsvSource({"0, 13", "4.712389, 15", "14.137167, 11"})
  void breathesBetweenTheLightBounds(final float ageTicks, final int expected) {
    assertEquals(expected, GlowPulse.blockLight(ageTicks));
  }

  @ParameterizedTest
  @ValueSource(floats = {0f, 1.5f, 7f, 33.3f, 1000f})
  void neverLeavesTheBounds(final float ageTicks) {
    final int light = GlowPulse.blockLight(ageTicks);

    assertTrue(light >= GlowPulse.MIN_BLOCK_LIGHT && light <= GlowPulse.MAX_BLOCK_LIGHT);
  }
}
