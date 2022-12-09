package com.qolbasics.handler;

import com.mojang.logging.LogUtils;
import com.qolbasics.config.QOLBasicsConfig;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

public class TotemDeathHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if(!(event.getEntity() instanceof Player) || !isTotemInInventory((Player) event.getEntity()) || !QOLBasicsConfig.TOTEM_ACTIVE_IN_ENTIRE_INVENTORY.get()) {
            return;
        }

        Player player = (Player) event.getEntity();
        ItemStack totem = getTotemFromInventory(player);
        ItemStack itemstack = totem.copy();
        totem.shrink(1);

        if (player instanceof ServerPlayer) {
            ServerPlayer serverplayer = (ServerPlayer)player;
            serverplayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
            CriteriaTriggers.USED_TOTEM.trigger(serverplayer, itemstack);
        }

        player.setHealth(1.0F);
        player.removeAllEffects();
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
        player.level.broadcastEntityEvent(player, (byte)35);

        LOGGER.info("using totem from deep inventory");
        event.setCanceled(true);
    }

    public static boolean isTotemInInventory(Player player) {
        int slot = player.getInventory().findSlotMatchingItem(Items.TOTEM_OF_UNDYING.getDefaultInstance());
        return slot != -1;
    }

    public static ItemStack getTotemFromInventory(Player player) {
        int slot = player.getInventory().findSlotMatchingItem(Items.TOTEM_OF_UNDYING.getDefaultInstance());
        return player.getInventory().getItem(slot);
    }
}
