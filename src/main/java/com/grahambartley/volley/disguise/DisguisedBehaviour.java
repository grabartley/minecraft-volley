package com.grahambartley.volley.disguise;

import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;

public final class DisguisedBehaviour {
  private static final int WANDER_REACH = 6;
  private static final int WANDER_HEIGHT = 3;
  private static final double WANDER_SPEED = 0.8;
  private static final int DEFUSED = -1;

  private DisguisedBehaviour() {}

  public static void begin(final MobEntity mob) {
    mob.getNavigation().stop();
    mob.clearActiveItem();
    mob.setAttacking(false);
    if (mob instanceof CreeperEntity creeper) {
      creeper.setFuseSpeed(DEFUSED);
    }
  }

  public static void tick(final MobEntity mob) {
    if (mob instanceof PathAwareEntity walker
        && WanderRoll.fires(walker.getNavigation().isIdle(), walker.getRandom())) {
      wander(walker);
    }
    mob.getNavigation().tick();
    mob.getMoveControl().tick();
    mob.getLookControl().tick();
    mob.getJumpControl().tick();
  }

  public static void wander(final PathAwareEntity walker) {
    final Vec3d spot = NoPenaltyTargeting.find(walker, WANDER_REACH, WANDER_HEIGHT);
    if (spot != null) {
      walker.getNavigation().startMovingTo(spot.x, spot.y, spot.z, WANDER_SPEED);
    }
  }
}
