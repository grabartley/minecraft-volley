package com.grahambartley.volley.disguise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.minecraft.util.math.random.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class WanderRollTest {

  @ParameterizedTest
  @CsvSource({"0, true", "1, false", "79, false"})
  void anIdleMobWandersOnlyOnALuckyRoll(final int roll, final boolean expected) {
    final Random random = mock(Random.class);
    when(random.nextInt(WanderRoll.CHANCE)).thenReturn(roll);

    assertEquals(expected, WanderRoll.fires(true, random));
  }

  @Test
  void aBusyMobNeverRolls() {
    final Random random = mock(Random.class);

    assertEquals(false, WanderRoll.fires(false, random));
    verify(random, never()).nextInt(WanderRoll.CHANCE);
  }
}
