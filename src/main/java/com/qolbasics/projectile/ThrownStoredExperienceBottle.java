package com.qolbasics.projectile;

import com.qolbasics.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ThrownStoredExperienceBottle extends ThrowableItemProjectile {
    private int expAmount;
    public ThrownStoredExperienceBottle(EntityType<? extends net.minecraft.world.entity.projectile.ThrownExperienceBottle> entityType, Level level, int expAmount) {
        super(entityType, level);
        this.expAmount = expAmount;
    }

    public ThrownStoredExperienceBottle(Level level, LivingEntity livingEntity, int expAmount) {
        super(EntityType.EXPERIENCE_BOTTLE, livingEntity, level);
        this.expAmount = expAmount;
    }

    public ThrownStoredExperienceBottle(Level level, double v1, double v2, double v3) {
        super(EntityType.EXPERIENCE_BOTTLE, v1, v2, v3, level);
    }

    protected Item getDefaultItem() {
        return ModItems.StoredExpBottle.get();
    }

    protected float getGravity() {
        return 0.07F;
    }

    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (this.level instanceof ServerLevel) {
            this.level.levelEvent(2002, this.blockPosition(), PotionUtils.getColor(Potions.WATER));
            ExperienceOrb.award((ServerLevel)this.level, this.position(), ;
            this.discard();
        }

    }
}
