package com.qolbasics.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
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

    public static BlockHitResult getPlayerLookingAt(Level level, Player player, ClipContext.Fluid fluid) {
        // not sure what fluid does. see bucket impl for example.
        float f = player.getXRot();
        float f1 = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getReachDistance();
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return level.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, fluid, player));
    }
}
