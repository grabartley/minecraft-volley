package com.grahambartley.volley.arrow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.grahambartley.volley.Volley;
import net.minecraft.util.Identifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FlightSpinTest {

  @ParameterizedTest
  @CsvSource({
    "true, 0, 0",
    "true, 1, 40",
    "true, 2.5, 100",
    "true, 9, 0",
    "true, 10, 40",
    "false, 5, 0",
  })
  void rollsOnlyWhileFlying(final boolean flying, final float ageTicks, final float expected) {
    assertEquals(expected, FlightSpin.rollDegrees(flying, ageTicks), 1e-3);
  }

  @ParameterizedTest
  @CsvSource({"drill_arrow, true", "boomerang_arrow, true", "tnt_arrow, false"})
  void spinsOnlyForBoringAndBouncingArrows(final String path, final boolean expected) {
    assertEquals(expected, FlightSpin.spins(Identifier.of(Volley.MOD_ID, path)));
  }

  @Test
  void rejectsANullId() {
    assertThrows(NullPointerException.class, () -> FlightSpin.spins(null));
  }
}
