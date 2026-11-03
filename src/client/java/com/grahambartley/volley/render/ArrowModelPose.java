package com.grahambartley.volley.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public final class ArrowModelPose {
  public static final float VANILLA_PIXEL = 0.05625f;
  public static final float VANILLA_LENGTH = 16 * VANILLA_PIXEL;
  public static final float VANILLA_TIP = 4 * VANILLA_PIXEL;
  public static final float MODEL_DIAGONAL = (float) Math.sqrt(2);
  public static final float SCALE = VANILLA_LENGTH / MODEL_DIAGONAL;
  public static final float TIP_SHIFT = VANILLA_TIP - VANILLA_LENGTH / 2;
  private static final float HEADING_OFFSET = 90f;
  private static final float MODEL_TILT = -45f;
  private static final float SHAKE_SPEED = 3f;

  private ArrowModelPose() {}

  public static void orient(
      final MatrixStack matrices,
      final float yaw,
      final float pitch,
      final float shakeLeft,
      final float rollDegrees) {
    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw - HEADING_OFFSET));
    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(pitch));
    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(shakeDegrees(shakeLeft)));
    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rollDegrees));
    matrices.translate(TIP_SHIFT, 0f, 0f);
    matrices.scale(SCALE, SCALE, SCALE);
    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MODEL_TILT));
  }

  public static float shakeDegrees(final float shakeLeft) {
    return shakeLeft > 0 ? (float) -Math.sin(shakeLeft * SHAKE_SPEED) * shakeLeft : 0f;
  }
}
