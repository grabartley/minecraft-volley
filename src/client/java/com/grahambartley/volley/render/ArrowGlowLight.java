package com.grahambartley.volley.render;

import com.grahambartley.volley.arrow.GlowPulse;
import net.minecraft.client.render.LightmapTextureManager;

public final class ArrowGlowLight {
  private ArrowGlowLight() {}

  public static int pulse(final float ageTicks, final int packedLight) {
    return LightmapTextureManager.pack(
        GlowPulse.blockLight(
            ageTicks, LightmapTextureManager.getBlockLightCoordinates(packedLight)),
        LightmapTextureManager.getSkyLightCoordinates(packedLight));
  }
}
