package com.grahambartley.volley.render;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ArrowModelPoseTest {
  private static final float CLOSE = 1.0e-4f;

  @ParameterizedTest
  @CsvSource({"0, 0", "90, 0", "180, 30", "-45, -60", "270, 89"})
  void theModelTipLandsWhereTheVanillaTipWas(final float yaw, final float pitch) {
    assertClose(vanilla(yaw, pitch, 4), model(yaw, pitch, 1));
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "90, 0", "180, 30", "-45, -60"})
  void theModelTailLandsWhereTheVanillaTailWas(final float yaw, final float pitch) {
    assertClose(vanilla(yaw, pitch, -12), model(yaw, pitch, 0));
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "-1, 0", "-0.5, 0", "1, -0.1411", "2.5, -2.3450"})
  void shakesOnlyWhileShakeIsLeft(final float shakeLeft, final float expected) {
    assertEquals(expected, ArrowModelPose.shakeDegrees(shakeLeft), CLOSE);
  }

  private static Vector3f model(final float yaw, final float pitch, final float corner) {
    final MatrixStack matrices = new MatrixStack();
    ArrowModelPose.orient(matrices, yaw, pitch, 0f, 0f);
    return matrices
        .peek()
        .getPositionMatrix()
        .transformPosition(new Vector3f(corner - 0.5f, corner - 0.5f, 0f));
  }

  private static Vector3f vanilla(final float yaw, final float pitch, final float pixelX) {
    final MatrixStack matrices = new MatrixStack();
    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw - 90f));
    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(pitch));
    matrices.scale(0.05625f, 0.05625f, 0.05625f);
    matrices.translate(-4f, 0f, 0f);
    return matrices.peek().getPositionMatrix().transformPosition(new Vector3f(pixelX + 4f, 0f, 0f));
  }

  private static void assertClose(final Vector3f expected, final Vector3f actual) {
    assertEquals(expected.x, actual.x, CLOSE, "x");
    assertEquals(expected.y, actual.y, CLOSE, "y");
    assertEquals(expected.z, actual.z, CLOSE, "z");
  }
}
