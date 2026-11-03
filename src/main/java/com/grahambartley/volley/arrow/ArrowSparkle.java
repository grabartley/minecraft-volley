package com.grahambartley.volley.arrow;

import net.minecraft.particle.DustParticleEffect;
import org.joml.Vector3f;

public final class ArrowSparkle {
  public static final float TRAIL_SCALE = 0.6f;
  public static final float GLOW_TRAIL_SCALE = 0.9f;
  public static final float BURST_SCALE = 1.1f;
  public static final int BURST_COUNT = 10;
  public static final int GLOW_BURST_COUNT = 18;

  private ArrowSparkle() {}

  public static DustParticleEffect dust(final int rgb, final float scale) {
    return new DustParticleEffect(
        new Vector3f(((rgb >> 16) & 0xFF) / 255f, ((rgb >> 8) & 0xFF) / 255f, (rgb & 0xFF) / 255f),
        scale);
  }

  public static boolean trails(final boolean inGround, final boolean removed, final int age) {
    return !inGround && !removed && age > 1;
  }

  public static float trailScale(final boolean glows) {
    return glows ? GLOW_TRAIL_SCALE : TRAIL_SCALE;
  }

  public static int burstCount(final boolean glows) {
    return glows ? GLOW_BURST_COUNT : BURST_COUNT;
  }
}
