package com.qolbasics.utils;

import com.google.common.collect.Lists;
import com.qolbasics.item.ModItems;
import com.qolbasics.item.StoredExperience;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ItemUtils {
    private static final Style LORE_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(true);
    public static ItemEntity givePlayerItemOrDrop(Player player, ItemStack itemStack) {
        if (!player.addItem(itemStack)) {
            // failed to give item due to inventory full
            return player.drop(itemStack, false, false);
        }
        return null;
    }

    public static ItemStack getNewStoredExpBottle(int expAmount, int expLevel, float expProgress) {
        ItemStack storedExpBottleItem = ModItems.StoredExpBottle.get().getDefaultInstance();
        storedExpBottleItem.getCapability(StoredExperience.INSTANCE).ifPresent(storedExperience -> {
            storedExperience.setExpTotal(expAmount);
            storedExperience.setExpLevel(expLevel);
            storedExperience.setExpProgress(expProgress);
        });
        Float levelFloat = expLevel + expProgress;
        return setLore(storedExpBottleItem, Arrays.asList("Total Exp: " + expAmount, String.format("Level: %.2f", levelFloat)));
    }

    public static ItemStack setLore(ItemStack itemStack, List<String> loreLines) {
        if(loreLines.size() == 0) {
            return itemStack;
        }
        CompoundTag displayTag = itemStack.getOrCreateTagElement("display");
        ListTag loreTag = new ListTag();
        for(String line : loreLines) {
            loreTag.add(StringTag.valueOf("{\"text\":\"" + line + "\"}"));
        }

        displayTag.put("Lore", loreTag);
        return itemStack;
    }

    public static ItemStack setName(ItemStack itemStack, String nameText) {
        CompoundTag displayTag = itemStack.getOrCreateTagElement("display");
        StringTag nameTag = StringTag.valueOf("{\"text\":\"" + nameText + "\"}");
        displayTag.put("Name", nameTag);
        return itemStack;
    }
}


