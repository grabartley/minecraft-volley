package com.grahambartley.volley.arrow;

public final class GlowPulse {
  public static final int MIN_BLOCK_LIGHT = 11;
  public static final int MAX_BLOCK_LIGHT = 15;
  private static final float TICKS_PER_RADIAN = 3f;

  private GlowPulse() {}

  public static int blockLight(final float ageTicks, final int ambientBlockLight) {
    return Math.max(ambientBlockLight, blockLight(ageTicks));
  }

  public static int blockLight(final float ageTicks) {
    final double wave = (Math.sin(ageTicks / TICKS_PER_RADIAN) + 1) / 2;
    return (int) Math.round(MIN_BLOCK_LIGHT + wave * (MAX_BLOCK_LIGHT - MIN_BLOCK_LIGHT));
  }
}
