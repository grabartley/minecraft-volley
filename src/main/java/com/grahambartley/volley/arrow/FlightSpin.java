package com.grahambartley.volley.arrow;

public final class FlightSpin {
  public static final float DEGREES_PER_TICK = 40f;
  private static final float FULL_TURN = 360f;

  private FlightSpin() {}

  public static float rollDegrees(final boolean flying, final float ageTicks) {
    return flying ? (ageTicks * DEGREES_PER_TICK) % FULL_TURN : 0f;
  }
}
