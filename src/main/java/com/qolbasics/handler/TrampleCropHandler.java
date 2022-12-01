package com.qolbasics.handler;

import com.qolbasics.config.QOLBasicsConfig;
import com.mojang.logging.LogUtils;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import static com.qolbasics.utils.EntityUtils.isWearingFeatherFalling;

public class TrampleCropHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onHarvest(BlockEvent.FarmlandTrampleEvent event) {
        if(!isWearingFeatherFalling(event.getEntity()) || !QOLBasicsConfig.FEATHER_FALL_AVOIDS_TRAMPLE.get()) {
            return;
        }
        LOGGER.info("Canceling Trample Event! Player is wearing FeatherFalling boots!");
        event.setCanceled(true);
    }
}
