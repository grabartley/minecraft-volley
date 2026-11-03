package com.grahambartley.volley.disguise;

import net.minecraft.util.math.random.Random;

public final class WanderRoll {
  public static final int CHANCE = 80;

  private WanderRoll() {}

  public static boolean fires(final boolean idle, final Random random) {
    return idle && random.nextInt(CHANCE) == 0;
  }
}
