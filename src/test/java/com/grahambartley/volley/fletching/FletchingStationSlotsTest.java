package com.grahambartley.volley.fletching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FletchingStationSlotsTest {

  @Test
  void offersOneInputSlotForEachOfTheNineIngredientsARecipeMayDeclare() {
    assertEquals(9, FletchingStationSlots.INPUT_COUNT);
  }

  @Test
  void countsInputsResultAndTheWholePlayerInventory() {
    assertEquals(46, FletchingStationSlots.TOTAL_SLOTS);
  }

  @Test
  void putsTheResultAfterEveryInputAndThePlayerInventoryAfterTheResult() {
    assertEquals(9, FletchingStationSlots.RESULT_SLOT);
    assertEquals(10, FletchingStationSlots.FIRST_PLAYER_SLOT);
    assertEquals(37, FletchingStationSlots.FIRST_HOTBAR_SLOT);
  }

  @ParameterizedTest(name = "slot {0}")
  @CsvSource({"0, input", "8, input", "9, result", "10, player", "45, player"})
  void assignsEverySlotIndexToExactlyOneRegion(final int slot, final String region) {
    assertEquals("input".equals(region), FletchingStationSlots.isInput(slot));
    assertEquals("result".equals(region), FletchingStationSlots.isResult(slot));
  }

  @ParameterizedTest(name = "slot {0}")
  @ValueSource(ints = {-1, 46})
  void claimsNoRegionForASlotOutsideTheHandler(final int slot) {
    assertFalse(FletchingStationSlots.isInput(slot));
    assertFalse(FletchingStationSlots.isResult(slot));
  }
}
