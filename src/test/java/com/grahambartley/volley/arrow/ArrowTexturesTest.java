package com.grahambartley.volley.arrow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import net.minecraft.util.Identifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ArrowTexturesTest {

  @ParameterizedTest
  @CsvSource({"volley", "minecraft"})
  void sharesOnePartsTexturePerNamespace(final String namespace) {
    assertEquals(Identifier.of(namespace, "item/arrow_parts"), ArrowTextures.itemParts(namespace));
  }

  @Test
  void rejectsANullNamespace() {
    assertThrows(NullPointerException.class, () -> ArrowTextures.itemParts(null));
  }
}
