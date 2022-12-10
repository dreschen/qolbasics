package com.qolbasics.handler;

import com.mojang.logging.LogUtils;
import com.qolbasics.config.QOLBasicsConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import static com.qolbasics.utils.ItemUtils.getNewStoredExpBottle;
import static com.qolbasics.utils.ItemUtils.givePlayerItemOrDrop;

public class ExpBottleStoreHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {

        //Be sure to use if(event.phase == Phase.END), so your code not runs twice.
        Player player = event.getEntity();
        float expProgress = player.experienceProgress;
        int expLevel = player.experienceLevel;
        int expTotal = player.totalExperience;
        ItemStack itemstack = player.getItemInHand(event.getHand());

        if (!itemstack.is(Items.GLASS_BOTTLE) || !QOLBasicsConfig.BETTER_EXP_STORAGE_ENABLED.get() || !(expTotal > 0)) {
            return;
        }

        itemstack.shrink(1);
        ItemStack expBottle = getNewStoredExpBottle(expTotal);
        givePlayerItemOrDrop(player, expBottle); // We don't care about the itemEntity if it gets dropped.
        player.totalExperience = 0;
        player.experienceLevel = 0;
        player.experienceProgress = 0;


        //player is holding a glass bottle and this event is enabled;
        LOGGER.info("Canceling RightClick Event! Player is storing Exp!");
        event.setCanceled(true);
    }
}
