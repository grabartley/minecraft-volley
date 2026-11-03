package com.grahambartley.volley.render;

import com.grahambartley.volley.arrow.FlightSpin;
import com.grahambartley.volley.arrow.GlowPulse;
import com.grahambartley.volley.entity.BaseArrowEntity;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class BaseArrowEntityRenderer<E extends BaseArrowEntity>
    extends ProjectileEntityRenderer<E> {
  private final Identifier texture;
  private final Optional<Identifier> glowTexture;
  private final boolean spins;
  private boolean drawingGlow;

  public BaseArrowEntityRenderer(
      final EntityRendererFactory.Context context, final Identifier texture) {
    this(context, texture, Optional.empty(), false);
  }

  public BaseArrowEntityRenderer(
      final EntityRendererFactory.Context context,
      final Identifier texture,
      final Optional<Identifier> glowTexture,
      final boolean spins) {
    super(context);
    this.spins = spins;
    this.texture = Objects.requireNonNull(texture, "texture");
    this.glowTexture = Objects.requireNonNull(glowTexture, "glowTexture");
  }

  @Override
  public void render(
      final E entity,
      final float yaw,
      final float tickDelta,
      final MatrixStack matrices,
      final VertexConsumerProvider vertexConsumers,
      final int light) {
    matrices.push();
    if (spins) {
      roll(entity, tickDelta, matrices);
    }
    drawWithGlow(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    matrices.pop();
  }

  private void roll(final E entity, final float tickDelta, final MatrixStack matrices) {
    final float heading = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90f;
    final float pitch = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());
    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(heading));
    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(pitch));
    matrices.multiply(
        RotationAxis.POSITIVE_X.rotationDegrees(
            FlightSpin.rollDegrees(entity.inFlight(), entity.age + tickDelta)));
    matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(pitch));
    matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(heading));
  }

  private void drawWithGlow(
      final E entity,
      final float yaw,
      final float tickDelta,
      final MatrixStack matrices,
      final VertexConsumerProvider vertexConsumers,
      final int light) {
    super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    if (glowTexture.isEmpty()) {
      return;
    }
    drawingGlow = true;
    try {
      super.render(
          entity,
          yaw,
          tickDelta,
          matrices,
          vertexConsumers,
          LightmapTextureManager.pack(
              GlowPulse.blockLight(entity.age + tickDelta),
              LightmapTextureManager.getSkyLightCoordinates(light)));
    } finally {
      drawingGlow = false;
    }
  }

  @Override
  public Identifier getTexture(final E entity) {
    return drawingGlow ? glowTexture.orElseThrow() : texture;
  }
}
