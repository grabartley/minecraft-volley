package com.grahambartley.volley.render;

import com.grahambartley.volley.item.TintedArrowItems;
import com.grahambartley.volley.tint.TintChoice;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public final class TintedArrowColors {
  public static final int TINTED_LAYER = 0;

  private TintedArrowColors() {}

  public static void registerAll() {
    TintedArrowItems.registered()
        .forEach(
            item ->
                ColorProviderRegistry.ITEM.register(
                    (stack, tintIndex) ->
                        layerColor(tintIndex, item.choiceOf(stack), Registries.ITEM.getId(item)),
                    item));
  }

  public static int layerColor(
      final int tintIndex, final TintChoice choice, final Identifier arrowId) {
    return tintIndex == TINTED_LAYER
        ? opaque(choice)
        : FletchingColors.layerColor(tintIndex, arrowId);
  }

  public static int opaque(final TintChoice choice) {
    return ColorHelper.Argb.fullAlpha(choice.color());
  }
}
