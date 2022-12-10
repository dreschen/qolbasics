package com.qolbasics.item;

import com.qolbasics.projectile.ThrownStoredExperienceBottle;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

public class StoredExpBottleItem extends Item {
    public StoredExpBottleItem(Item.Properties p_41194_, int expAmount) {
        super(p_41194_);
    }

    public boolean isFoil(ItemStack itemStack) {
        return true;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        int expAmount = 0;
        ItemStack itemstack = player.getItemInHand(interactionHand);
        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            LazyOptional<StoredExperience> optional = itemstack.getCapability(StoredExperienceProvider.STORED_EXPERIENCE);
            if(optional.isPresent()){
                expAmount = optional.resolve().get().getExpAmount();
            }
            ThrownStoredExperienceBottle thrownstoredexperiencebottle = new ThrownStoredExperienceBottle(level, player);
            thrownstoredexperiencebottle.setItem(itemstack);
            thrownstoredexperiencebottle.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.7F, 1.0F);
            level.addFreshEntity(thrownstoredexperiencebottle);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}