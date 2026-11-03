package com.grahambartley.volley.arrow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.grahambartley.volley.Volley;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.util.Identifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ArrowAuraTest {
  private static final String ITEM_MODELS = "src/main/resources/assets/volley/models/item";

  static List<String> arrowPaths() throws IOException {
    try (Stream<Path> files = Files.list(Path.of(ITEM_MODELS))) {
      return files
          .map(file -> file.getFileName().toString().replace(".json", ""))
          .filter(name -> name.endsWith("_arrow"))
          .sorted()
          .toList();
    }
  }

  @Test
  void everyTableEntryNamesARealArrow() throws IOException {
    final List<String> arrows = arrowPaths();

    assertTrue(arrows.containsAll(ArrowAura.colouredPaths()), "colours");
    assertTrue(arrows.containsAll(ArrowAura.glowingPaths()), "glows");
  }

  @ParameterizedTest
  @MethodSource("arrowPaths")
  void everyArrowHasItsOwnAccent(final String path) {
    assertNotEquals(ArrowAura.DEFAULT_COLOR, ArrowAura.color(Identifier.of(Volley.MOD_ID, path)));
  }

  @ParameterizedTest
  @CsvSource({
    "shock_arrow, true",
    "frost_arrow, true",
    "torch_arrow, true",
    "tnt_arrow, false",
    "rope_arrow, false",
    "missing_arrow, false",
  })
  void glowsOnlyForEnergeticArrows(final String path, final boolean expected) {
    assertEquals(expected, ArrowAura.glows(Identifier.of(Volley.MOD_ID, path)));
  }

  @ParameterizedTest
  @CsvSource({
    "tnt_arrow, 0xDB3B28",
    "frost_arrow, 0xBFE8FF",
    "courier_arrow, 0x2E9C96",
    "missing_arrow, 0xE2D6BE",
  })
  void picksTheArrowAccentOrTheDefault(final String path, final String expected) {
    assertEquals(Integer.decode(expected), ArrowAura.color(Identifier.of(Volley.MOD_ID, path)));
  }

  @Test
  void rejectsNullIds() {
    assertThrows(NullPointerException.class, () -> ArrowAura.color(null));
    assertThrows(NullPointerException.class, () -> ArrowAura.glows(null));
  }
}
