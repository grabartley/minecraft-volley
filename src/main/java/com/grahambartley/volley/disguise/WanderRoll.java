package com.grahambartley.volley.disguise;

public final class WanderRoll {
  public static final int CHANCE = 80;

  private WanderRoll() {}

  public static boolean fires(final boolean idle, final int roll) {
    return idle && roll == 0;
  }
}
