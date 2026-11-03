package com.grahambartley.volley.arrow;

import java.util.Objects;
import java.util.Set;
import net.minecraft.util.Identifier;

public final class FlightSpin {
  public static final float DEGREES_PER_TICK = 40f;
  private static final float FULL_TURN = 360f;
  private static final Set<String> SPINNING =
      Set.of("boomerang_arrow", "drill_arrow", "railgun_arrow", "ricochet_arrow");

  private FlightSpin() {}

  public static boolean spins(final Identifier arrowId) {
    Objects.requireNonNull(arrowId, "arrowId");
    return SPINNING.contains(arrowId.getPath());
  }

  public static float rollDegrees(final boolean flying, final float ageTicks) {
    return flying ? (ageTicks * DEGREES_PER_TICK) % FULL_TURN : 0f;
  }
}
