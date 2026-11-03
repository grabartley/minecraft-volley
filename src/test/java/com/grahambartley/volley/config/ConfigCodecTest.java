package com.grahambartley.volley.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ConfigCodecTest {
  private static final int DEFAULT_SYNC_BUDGET = 8192;

  @Test
  void roundTripsDefaults() {
    final VolleyConfig defaults = VolleyConfig.defaults();

    assertEquals(defaults, ConfigCodec.decode(ConfigCodec.encode(defaults)));
  }

  @Test
  void roundTripsFullyCustomisedValues() {
    final VolleyConfig original = ConfigSamples.everyFamilyCustomised();

    assertEquals(original, ConfigCodec.decode(ConfigCodec.encode(original)));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   ", "\n"})
  void decodesAbsentContentAsDefaults(final String json) {
    assertEquals(VolleyConfig.defaults(), ConfigCodec.decode(json));
  }

  @ParameterizedTest
  @ValueSource(strings = {"{ not json ", "[1,2,3]", "\"a string\"", "12", "null"})
  void refusesToDecodeContentThatIsNotAConfigObject(final String json) {
    assertThrows(JsonParseException.class, () -> ConfigCodec.decode(json));
  }

  @ParameterizedTest
  @ValueSource(strings = {"{ not json ", "[1,2,3]", "\"a string\"", "12", "null"})
  void fallsBackToDefaultsWhenAskedToBeLenient(final String json) {
    assertEquals(VolleyConfig.defaults(), ConfigCodec.decodeOrDefaults(json));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   ", "{ not json ", "[1,2,3]", "\"a string\"", "12", "null"})
  void decodesNothingCompleteFromContentThatIsNotAConfigObject(final String json) {
    assertTrue(ConfigCodec.decodeComplete(json).isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"{}", "{ \"grapple\": 5 }", "{ \"sound\": {} }"})
  void decodesNothingCompleteFromAnObjectMissingAFamily(final String json) {
    assertTrue(ConfigCodec.decodeComplete(json).isEmpty());
  }

  @Test
  void decodesNothingCompleteWhenAFamilyIsNotAnObject() {
    final JsonObject root = VolleyConfig.defaults().toJson();
    root.addProperty("grapple", 5);

    assertTrue(ConfigCodec.decodeComplete(root.toString()).isEmpty());
  }

  @Test
  void decodesCompleteTheConfigThatWasEncoded() {
    final VolleyConfig original = VolleyConfig.defaults().withSound(new SoundConfig(0.35f));

    assertEquals(Optional.of(original), ConfigCodec.decodeComplete(ConfigCodec.encode(original)));
  }

  @Test
  void reportsAParseFailureRatherThanReturningNull() {
    assertThrows(JsonParseException.class, () -> ConfigCodec.decode("\"a string\""));
  }

  @Test
  void encodesDefaultsWellWithinTheWireLimit() {
    assertTrue(
        ConfigCodec.encode(VolleyConfig.defaults()).length() < ConfigCodec.MAX_ENCODED_LENGTH);
  }

  @Test
  void encodesAMaximallyPopulatedConfigWithinTheWireLimit() {
    final VolleyConfig maximal =
        VolleyConfig.defaults()
            .withPhysics(
                new PhysicsArrowConfig(
                    8,
                    longestIdentifiers(PhysicsArrowConfig.GRAVITY_BLOCK_EXCLUSIONS_MAX),
                    16,
                    false))
            .withDiscovery(
                DiscoveryArrowConfig.defaults()
                    .withProspector(
                        ProspectorArrowConfig.defaults()
                            .withBlocks(longestIdentifiers(ProspectorArrowConfig.BLOCKS_MAX))))
            .withSocial(
                SocialArrowConfig.defaults()
                    .withCourier(
                        CourierArrowConfig.defaults()
                            .withUndeliverable(
                                longestIdentifiers(CourierArrowConfig.UNDELIVERABLE_MAX))));

    assertEquals(
        PhysicsArrowConfig.GRAVITY_BLOCK_EXCLUSIONS_MAX,
        maximal.physics().gravityBlockExclusions().size());
    assertEquals(
        ProspectorArrowConfig.BLOCKS_MAX, maximal.discovery().prospector().blocks().size());
    assertEquals(
        CourierArrowConfig.UNDELIVERABLE_MAX, maximal.social().courier().undeliverable().size());
    assertTrue(ConfigCodec.encode(maximal).length() < ConfigCodec.MAX_ENCODED_LENGTH);
  }

  @Test
  void encodesDefaultsWithinTheSyncBudget() {
    assertTrue(ConfigCodec.encode(VolleyConfig.defaults()).length() < DEFAULT_SYNC_BUDGET);
  }

  private static List<String> longestIdentifiers(final int count) {
    return IntStream.range(0, count).mapToObj(i -> padded("minecraft:block_" + i + "_")).toList();
  }

  private static String padded(final String prefix) {
    return prefix + "x".repeat(ConfigValues.MAX_IDENTIFIER_LENGTH - prefix.length());
  }
}
