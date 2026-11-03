package com.grahambartley.volley.arrow;

import java.util.Objects;
import net.minecraft.util.Identifier;

public final class ArrowTextures {
  private static final String ITEM_PARTS_PATH = "item/arrow_parts";

  private ArrowTextures() {}

  public static Identifier itemParts(final String namespace) {
    Objects.requireNonNull(namespace, "namespace");
    return Identifier.of(namespace, ITEM_PARTS_PATH);
  }
}
