package com.qolbasics.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityUtils {
    public static boolean isWearingFeatherFalling(Entity entity) {
        ItemStack boots = getBoots(entity);
        return hasFeatherFalling(boots);
    }

    public static ItemStack getBoots(Entity entity) {
        for(ItemStack itemStack : entity.getArmorSlots()) {
            Item item = itemStack.getItem();
            if (item instanceof ArmorItem) {
                if (((ArmorItem) item).getSlot() == EquipmentSlot.FEET)
                    return itemStack;
            }
        }
        return null;
    }

    public static boolean hasFeatherFalling(ItemStack item) {
        if (item == null) {
            return false;
        }

        Integer featherFallLevel = EnchantmentHelper.getEnchantments(item).get(Enchantments.FALL_PROTECTION);
        if(featherFallLevel != null) {
            return featherFallLevel > 0;
        }
        return false;
    }
}
