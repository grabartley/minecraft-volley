package com.grahambartley.volley.render;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minecraft.client.render.LightmapTextureManager;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ArrowGlowLightTest {

  @ParameterizedTest
  @CsvSource({
    "0, 0, 15, 13, 15",
    "0, 15, 0, 15, 0",
    "0, 4, 7, 13, 7",
    "4.712389, 0, 15, 15, 15",
    "14.137167, 0, 0, 11, 0"
  })
  void raisesBlockLightToThePulseAndKeepsSkyLight(
      final float ageTicks,
      final int block,
      final int sky,
      final int expectedBlock,
      final int expectedSky) {
    final int lit = ArrowGlowLight.pulse(ageTicks, LightmapTextureManager.pack(block, sky));

    assertEquals(expectedBlock, LightmapTextureManager.getBlockLightCoordinates(lit));
    assertEquals(expectedSky, LightmapTextureManager.getSkyLightCoordinates(lit));
  }
}
