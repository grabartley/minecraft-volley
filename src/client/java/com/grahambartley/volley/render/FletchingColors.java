package com.grahambartley.volley.render;

import com.grahambartley.volley.ModArrows;
import com.grahambartley.volley.arrow.ArrowAura;
import com.grahambartley.volley.item.TintedArrowItem;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public final class FletchingColors {
  public static final int FLETCHING_LAYER = 1;

  private FletchingColors() {}

  public static void registerAll() {
    ModArrows.registered().stream()
        .filter(arrow -> !(arrow.item() instanceof TintedArrowItem))
        .forEach(
            arrow ->
                ColorProviderRegistry.ITEM.register(
                    (stack, tintIndex) -> layerColor(tintIndex, arrow.id()), arrow.item()));
  }

  public static int layerColor(final int tintIndex, final Identifier arrowId) {
    return tintIndex == FLETCHING_LAYER
        ? ColorHelper.Argb.fullAlpha(ArrowAura.color(arrowId))
        : Colors.WHITE;
  }
}
