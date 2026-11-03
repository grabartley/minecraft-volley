package com.grahambartley.volley.disguise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class WanderRollTest {

  @ParameterizedTest
  @CsvSource({
    "true, 0, true",
    "true, 1, false",
    "true, 79, false",
    "false, 0, false",
    "false, 5, false"
  })
  void onlyAnIdleMobOnALuckyRollWanders(
      final boolean idle, final int roll, final boolean expected) {
    assertEquals(expected, WanderRoll.fires(idle, roll));
  }
}
