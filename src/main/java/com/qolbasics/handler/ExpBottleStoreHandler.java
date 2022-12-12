package com.qolbasics.handler;

import com.mojang.logging.LogUtils;
import com.qolbasics.config.QOLBasicsConfig;
import com.qolbasics.network.ModMessages;
import com.qolbasics.network.StoreExpPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

import static com.qolbasics.utils.EntityUtils.getPlayerLookingAt;
import static com.qolbasics.utils.ItemUtils.getNewStoredExpBottle;
import static com.qolbasics.utils.ItemUtils.givePlayerItemOrDrop;

public class ExpBottleStoreHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {

        //Be sure to use if(event.phase == Phase.END), so your code not runs twice.

        //Get relevant event information
        Player player = event.getEntity();
        int expTotal = player.totalExperience;
        ItemStack itemstack = player.getItemInHand(event.getHand());


        // only run on client side. (return if player instanceof ServerPlayer)
        // if the player is not holding a glass bottle, just return
        // if the config disables this event, just return
        // if the player has no exp to store, just return

        boolean isGlassBottle = itemstack.is(Items.GLASS_BOTTLE);
        if (!QOLBasicsConfig.BETTER_EXP_STORAGE_ENABLED.get()
                || !(expTotal > 0)
                || !isGlassBottle
                || (player instanceof ServerPlayer)) {
            return;
        }

        // here, we are executing on the client, are holding a glass bottle, and have exp.
        // first we want to use the glass bottle as normal.
        //     If this interaction result is successful - we don't want to do anything else
        //     If this interaction is unsuccessful, we want to run our exp storage.

        boolean usedBottle = itemstack.use(event.getLevel(), player, event.getHand()).getResult().equals(InteractionResult.SUCCESS);
        if (usedBottle) {
            return;
        }

        // here, we did not use the bottle. Now, we have to run our handler on the server side.
        // to do this, we need to send our packet to the server. The handler for this packet will run the storage function
        LOGGER.info("Sending StoreExpPacket & Canceling right-click item event.");
        ModMessages.sendToServer(new StoreExpPacket());
        event.setCanceled(true);
    }

    public static void storeExpServerHandler(Player player) {
        //Get relevant event information
        float expProgress = player.experienceProgress;
        int expLevel = player.experienceLevel;
        int expTotal = player.totalExperience;

        // find which hand has a glass bottle
        ItemStack itemStack;
        ItemStack mainHandItemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHandItemstack = player.getItemInHand(InteractionHand.OFF_HAND);

        if(mainHandItemstack.is(Items.GLASS_BOTTLE)) {
            itemStack = mainHandItemstack;
        }
        else if(offHandItemstack.is(Items.GLASS_BOTTLE)) {
            itemStack = offHandItemstack;
        }
        else { // if neither hand has a glass bottle, give up
            return;
        }

        itemStack.shrink(1);
        ItemStack expBottle = getNewStoredExpBottle(expTotal, expLevel, expProgress);
        givePlayerItemOrDrop(player, expBottle); // If the players inventory is full, throw it on the ground.
        player.totalExperience = 0;
        player.experienceLevel = 0;
        player.experienceProgress = 0;
    }
}
