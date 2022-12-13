package com.qolbasics.events;

import com.qolbasics.item.ModItems;
import com.qolbasics.item.StoredExperienceProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.qolbasics.QOLBasicsMod.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesItem(final AttachCapabilitiesEvent<ItemStack> event) {
        if(event.getObject() != null && event.getObject().is(ModItems.StoredExpBottle.get())) {
            event.addCapability(StoredExperienceProvider.IDENTIFIER, new StoredExperienceProvider());
        }
    }
}
