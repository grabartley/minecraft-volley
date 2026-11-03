package com.grahambartley.volley.render;

import com.grahambartley.volley.arrow.FlightSpin;
import com.grahambartley.volley.arrow.GlowPulse;
import com.grahambartley.volley.entity.BaseArrowEntity;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BaseArrowEntityRenderer<E extends BaseArrowEntity> extends EntityRenderer<E> {
  private final ItemRenderer itemRenderer;
  private final Function<E, ItemStack> stackOf;
  private final boolean glows;
  private final boolean spins;

  public BaseArrowEntityRenderer(
      final EntityRendererFactory.Context context,
      final Function<E, ItemStack> stackOf,
      final boolean glows,
      final boolean spins) {
    super(context);
    this.itemRenderer = context.getItemRenderer();
    this.stackOf = Objects.requireNonNull(stackOf, "stackOf");
    this.glows = glows;
    this.spins = spins;
  }

  @Override
  public void render(
      final E entity,
      final float yaw,
      final float tickDelta,
      final MatrixStack matrices,
      final VertexConsumerProvider vertexConsumers,
      final int light) {
    final float age = entity.age + tickDelta;
    matrices.push();
    ArrowModelPose.orient(
        matrices,
        MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()),
        MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()),
        entity.shake - tickDelta,
        spins ? FlightSpin.rollDegrees(entity.inFlight(), age) : 0f);
    itemRenderer.renderItem(
        stackOf.apply(entity),
        ModelTransformationMode.NONE,
        glows ? glowLight(age, light) : light,
        OverlayTexture.DEFAULT_UV,
        matrices,
        vertexConsumers,
        entity.getWorld(),
        entity.getId());
    matrices.pop();
    super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
  }

  private static int glowLight(final float age, final int light) {
    return LightmapTextureManager.pack(
        GlowPulse.blockLight(age, LightmapTextureManager.getBlockLightCoordinates(light)),
        LightmapTextureManager.getSkyLightCoordinates(light));
  }

  @Override
  @SuppressWarnings("deprecation")
  public Identifier getTexture(final E entity) {
    return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
  }
}
