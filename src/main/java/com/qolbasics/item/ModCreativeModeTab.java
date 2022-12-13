package com.qolbasics.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab QOLBASICS_TAB = new CreativeModeTab("qolbasicstab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.StoredExpBottle.get());
        }
    };
}
