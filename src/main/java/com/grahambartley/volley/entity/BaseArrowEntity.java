package com.grahambartley.volley.entity;

import com.grahambartley.volley.ModSounds;
import com.grahambartley.volley.arrow.ArrowAura;
import com.grahambartley.volley.arrow.ArrowImpact;
import com.grahambartley.volley.arrow.ArrowSparkle;
import com.grahambartley.volley.audio.ModSoundPlayer;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class BaseArrowEntity extends PersistentProjectileEntity {
  private static final double GLANCE_SPEED_FACTOR = 0.2;
  private static final double RESTING_SPEED_SQUARED = 1.0E-7;
  private static final float DROP_HEIGHT = 0.1f;
  private static final double BURST_SPREAD = 0.25;
  private static final double BURST_SPEED = 0.02;
  private static final float CHIME_VOLUME = 0.6f;
  private static final float CHIME_PITCH = 1.6f;

  protected BaseArrowEntity(
      final EntityType<? extends BaseArrowEntity> entityType, final World world) {
    super(entityType, world);
  }

  protected BaseArrowEntity(
      final EntityType<? extends BaseArrowEntity> entityType,
      final World world,
      final double x,
      final double y,
      final double z,
      final ItemStack stack,
      @Nullable final ItemStack weapon) {
    super(entityType, x, y, z, world, stack, weapon);
  }

  @Override
  protected final void onBlockHit(final BlockHitResult blockHitResult) {
    if (!(getWorld() instanceof ServerWorld serverWorld)) {
      super.onBlockHit(blockHitResult);
      return;
    }
    burst(serverWorld);
    resolve(
        onArrowHitBlock(serverWorld, blockHitResult),
        () -> super.onBlockHit(blockHitResult),
        () -> afterArrowHitBlock(serverWorld, blockHitResult));
  }

  @Override
  protected final void onEntityHit(final EntityHitResult entityHitResult) {
    if (!(getWorld() instanceof ServerWorld serverWorld)) {
      super.onEntityHit(entityHitResult);
      return;
    }
    burst(serverWorld);
    final ArrowImpact impact = onArrowHitEntity(serverWorld, entityHitResult);
    resolve(
        hurtsWhatItHits() ? impact : impact.sparingWhatItHits(),
        () -> super.onEntityHit(entityHitResult),
        () -> afterArrowHitEntity(serverWorld, entityHitResult));
  }

  @Override
  public final void tick() {
    super.tick();

    if (getWorld() instanceof ServerWorld serverWorld && !isRemoved()) {
      onArrowTick(serverWorld);
    } else if (getWorld().isClient() && ArrowSparkle.trails(inGround, isRemoved(), age)) {
      trail();
    }
  }

  private void trail() {
    final boolean glows = ArrowAura.glows(arrowId());
    getWorld()
        .addParticle(
            ArrowSparkle.dust(ArrowAura.color(arrowId()), ArrowSparkle.trailScale(glows)),
            getX(),
            getY(),
            getZ(),
            0,
            0,
            0);
    if (ArrowSparkle.sparkles(glows, age)) {
      getWorld().addParticle(ParticleTypes.END_ROD, getX(), getY(), getZ(), 0, 0, 0);
    }
  }

  public boolean inFlight() {
    return !inGround;
  }

  public Optional<LivingEntity> shooter() {
    return getOwner() instanceof LivingEntity living ? Optional.of(living) : Optional.empty();
  }

  public Optional<PlayerEntity> shootingPlayer() {
    return getOwner() instanceof PlayerEntity player ? Optional.of(player) : Optional.empty();
  }

  protected boolean hurtsWhatItHits() {
    return true;
  }

  protected ArrowImpact onArrowHitBlock(
      final ServerWorld world, final BlockHitResult blockHitResult) {
    return ArrowImpact.DEFAULT;
  }

  protected ArrowImpact onArrowHitEntity(
      final ServerWorld world, final EntityHitResult entityHitResult) {
    return ArrowImpact.DEFAULT;
  }

  protected void afterArrowHitBlock(final ServerWorld world, final BlockHitResult blockHitResult) {}

  protected void afterArrowHitEntity(
      final ServerWorld world, final EntityHitResult entityHitResult) {}

  protected void onArrowTick(final ServerWorld world) {}

  protected ArrowImpact glanceOff(final Entity struck) {
    deflect(ProjectileDeflection.SIMPLE, struck, getOwner(), false);
    setVelocity(getVelocity().multiply(GLANCE_SPEED_FACTOR));
    if (getVelocity().lengthSquared() >= RESTING_SPEED_SQUARED) {
      return ArrowImpact.RETAIN;
    }
    if (pickupType == PickupPermission.ALLOWED) {
      dropStack(asItemStack(), DROP_HEIGHT);
    }
    return ArrowImpact.DISCARD;
  }

  @Override
  protected ItemStack getDefaultItemStack() {
    return new ItemStack(arrowItem());
  }

  protected Item arrowItem() {
    return Registries.ITEM.get(arrowId());
  }

  private Identifier arrowId() {
    return Registries.ENTITY_TYPE.getId(getType());
  }

  private void burst(final ServerWorld world) {
    final boolean glows = ArrowAura.glows(arrowId());
    world.spawnParticles(
        ArrowSparkle.dust(ArrowAura.color(arrowId()), ArrowSparkle.BURST_SCALE),
        getX(),
        getY(),
        getZ(),
        ArrowSparkle.burstCount(glows),
        BURST_SPREAD,
        BURST_SPREAD,
        BURST_SPREAD,
        BURST_SPEED);
    if (glows) {
      world.spawnParticles(
          ParticleTypes.END_ROD,
          getX(),
          getY(),
          getZ(),
          ArrowSparkle.GLOW_SPARK_COUNT,
          BURST_SPREAD,
          BURST_SPREAD,
          BURST_SPREAD,
          BURST_SPEED);
      ModSoundPlayer.playFrom(this, ModSounds.ARROW_GLOW_CHIME, CHIME_VOLUME, CHIME_PITCH);
    }
  }

  private void resolve(
      final ArrowImpact impact, final Runnable vanillaResolution, final Runnable afterResolution) {
    if (impact.runsVanillaResolution()) {
      vanillaResolution.run();
    }
    afterResolution.run();
    if (impact.removesArrow() && !isRemoved()) {
      discard();
    }
  }
}
