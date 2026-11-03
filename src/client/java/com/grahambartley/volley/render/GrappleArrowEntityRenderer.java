package com.grahambartley.volley.render;

import com.grahambartley.volley.entity.GrappleArrowEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.item.ItemStack;

public class GrappleArrowEntityRenderer extends BaseArrowEntityRenderer<GrappleArrowEntity> {

  public GrappleArrowEntityRenderer(
      final EntityRendererFactory.Context context, final ItemStack stack) {
    super(context, arrow -> stack, false, false);
  }

  @Override
  public boolean shouldRender(
      final GrappleArrowEntity arrow,
      final Frustum frustum,
      final double cameraX,
      final double cameraY,
      final double cameraZ) {
    return super.shouldRender(arrow, frustum, cameraX, cameraY, cameraZ)
        || arrow.getLeashHolder() != null;
  }
}
