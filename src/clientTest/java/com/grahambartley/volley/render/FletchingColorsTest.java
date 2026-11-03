package com.grahambartley.volley.render;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minecraft.util.Identifier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FletchingColorsTest {

  @ParameterizedTest
  @CsvSource({
    "tnt_arrow, 1, 0xFFDB3B28",
    "frost_arrow, 1, 0xFFBFE8FF",
    "tnt_arrow, 0, 0xFFFFFFFF",
    "tnt_arrow, -1, 0xFFFFFFFF",
    "missing_arrow, 1, 0xFFE2D6BE",
  })
  void tintsOnlyTheFletchingLayer(final String path, final int tintIndex, final String expected) {
    assertEquals(
        Long.decode(expected).intValue(),
        FletchingColors.layerColor(tintIndex, Identifier.of("volley", path)));
  }
}
