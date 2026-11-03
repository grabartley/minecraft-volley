package com.grahambartley.volley.render;

import com.grahambartley.volley.ModArrows;
import com.grahambartley.volley.arrow.ArrowAura;
import com.grahambartley.volley.arrow.FlightSpin;
import com.grahambartley.volley.arrow.RegisteredArrow;
import com.grahambartley.volley.entity.BaseArrowEntity;
import com.grahambartley.volley.entity.TintedArrowEntity;
import com.grahambartley.volley.item.TintedArrowItem;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public final class ArrowRendererRegistrar {
  private ArrowRendererRegistrar() {}

  public static void registerAll() {
    final Identifier grappleArrowId = ModArrows.GRAPPLE_ARROW.id();
    final ItemStack grappleStack = new ItemStack(ModArrows.GRAPPLE_ARROW.item());
    EntityRendererRegistry.register(
        ModArrows.GRAPPLE_ARROW.entityType(),
        context -> new GrappleArrowEntityRenderer(context, grappleStack));

    ModArrows.registered().stream()
        .filter(arrow -> !arrow.id().equals(grappleArrowId))
        .forEach(ArrowRendererRegistrar::register);
  }

  private static <E extends BaseArrowEntity> void register(final RegisteredArrow<E> arrow) {
    if (arrow.item() instanceof TintedArrowItem tinted) {
      registerTinted(arrow, tinted);
      return;
    }
    final ItemStack stack = new ItemStack(arrow.item());
    EntityRendererRegistry.register(
        arrow.entityType(),
        context ->
            new BaseArrowEntityRenderer<E>(
                context,
                entity -> stack,
                ArrowAura.glows(arrow.id()),
                FlightSpin.spins(arrow.id())));
  }

  @SuppressWarnings("unchecked")
  private static <E extends TintedArrowEntity> void registerTinted(
      final RegisteredArrow<?> arrow, final TintedArrowItem item) {
    EntityRendererRegistry.register(
        (EntityType<E>) arrow.entityType(),
        context ->
            new BaseArrowEntityRenderer<E>(
                context,
                entity -> item.stackOf(entity.tint()),
                ArrowAura.glows(arrow.id()),
                FlightSpin.spins(arrow.id())));
  }
}
