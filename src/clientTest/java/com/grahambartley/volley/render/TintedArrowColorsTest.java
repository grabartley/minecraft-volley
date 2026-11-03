package com.grahambartley.volley.render;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.grahambartley.volley.tint.TintChoice;
import net.minecraft.util.Identifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TintedArrowColorsTest {
  private static final Identifier PAINT = Identifier.of("volley", "paint_arrow");
  private static final TintChoice RED =
      new TintChoice("red", Identifier.ofVanilla("red_dye"), 0xB02E26, "color.minecraft.red");

  @Test
  void tintsTheTintedLayerWithTheChoiceFullyOpaque() {
    assertEquals(
        0xFFB02E26, TintedArrowColors.layerColor(TintedArrowColors.TINTED_LAYER, RED, PAINT));
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 2})
  void leavesEveryOtherLayerUntinted(final int tintIndex) {
    assertEquals(0xFFFFFFFF, TintedArrowColors.layerColor(tintIndex, RED, PAINT));
  }

  @Test
  void coloursTheFletchingWithTheArrowAccent() {
    assertEquals(
        0xFFE04FD0, TintedArrowColors.layerColor(FletchingColors.FLETCHING_LAYER, RED, PAINT));
  }

  @Test
  void keepsABlackChoiceVisibleRatherThanTransparent() {
    final TintChoice black =
        new TintChoice("black", Identifier.ofVanilla("black_dye"), 0, "color.minecraft.black");

    assertEquals(0xFF000000, TintedArrowColors.opaque(black));
  }
}
