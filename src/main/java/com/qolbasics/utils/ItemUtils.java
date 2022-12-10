package com.qolbasics.utils;

import com.qolbasics.item.ModItems;
import com.qolbasics.item.StoredExpBottleItem;
import com.qolbasics.item.StoredExperience;
import com.qolbasics.item.StoredExperienceProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ItemUtils {
    public static ItemEntity givePlayerItemOrDrop(Player player, ItemStack itemStack) {
        if (!player.addItem(itemStack)) {
            // failed to give item due to inventory full
            return player.drop(itemStack, true, false);
        }
        return null;
    }

    public static ItemStack getNewStoredExpBottle(int exp) {
        // I think I want to implement this as a potion or something
        ItemStack storedExpBottleItem = ModItems.StoredExpBottle.get().getDefaultInstance();
        storedExpBottleItem.getCapability(StoredExperienceProvider.STORED_EXPERIENCE).ifPresent(storedExperience -> storedExperience.setExpAmount(exp));
        return storedExpBottleItem;
    }
}
