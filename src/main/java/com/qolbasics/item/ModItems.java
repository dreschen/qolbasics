package com.qolbasics.item;

import com.qolbasics.QOLBasicsMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, QOLBasicsMod.MODID);

    public static final RegistryObject<Item> StoredExpBottle = ITEMS.register("stored_exp_bottle",
                () -> new StoredExpBottleItem((new Item.Properties()).tab(ModCreativeModeTab.QOLBASICS_TAB), 5));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
