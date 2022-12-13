package com.qolbasics.network;

import com.qolbasics.handler.ExpBottleStoreHandler;
import com.qolbasics.item.IStoredExperience;
import com.qolbasics.item.ModItems;
import com.qolbasics.item.StoredExperience;
import com.qolbasics.projectile.ThrownStoredExperienceBottle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ThrowStoredExpBottlePacket {

    public ThrowStoredExpBottlePacket() {

    }

    public ThrowStoredExpBottlePacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //HERE WE ARE ON THE SERVER
            ServerPlayer player = context.getSender();
            if(player == null) {
                return;
            }
            Level level = player.getLevel();
            // find which hand has a glass bottle
            ItemStack itemStack;
            ItemStack mainHandItemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack offHandItemstack = player.getItemInHand(InteractionHand.OFF_HAND);

            if(mainHandItemstack.is(ModItems.StoredExpBottle.get())) {
                itemStack = mainHandItemstack;
            }
            else if(offHandItemstack.is(ModItems.StoredExpBottle.get())) {
                itemStack = offHandItemstack;
            }
            else { // if neither hand has a storedExp bottle, give up
                return;
            }

            level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            int expAmount = 0;
            LazyOptional<IStoredExperience> optional = itemStack.getCapability(StoredExperience.INSTANCE);
            if(optional.resolve().isPresent()){
                expAmount = optional.resolve().get().getExpTotal();
            }
            ThrownStoredExperienceBottle thrownstoredexperiencebottle = new ThrownStoredExperienceBottle(level, player, expAmount);
            thrownstoredexperiencebottle.setItem(itemStack);
            thrownstoredexperiencebottle.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.7F, 1.0F);
            level.addFreshEntity(thrownstoredexperiencebottle);
            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        });
        return true;
    }
}