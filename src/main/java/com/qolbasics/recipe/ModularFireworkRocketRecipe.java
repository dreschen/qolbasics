package com.qolbasics.recipe;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.FireworkRocketRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class ModularFireworkRocketRecipe extends FireworkRocketRecipe {
    private static final Ingredient FIREWORK_ROCKET_INGREDIENT = Ingredient.of(Items.FIREWORK_ROCKET);
    private static final Ingredient GUNPOWDER_INGREDIENT = Ingredient.of(Items.GUNPOWDER);

    public ModularFireworkRocketRecipe(ResourceLocation id) {
        super(id);
    }

    public boolean matches(CraftingContainer container, Level level) {
        int fireworkCount = 0;
        int gunpowederCount = 0;

        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemstack = container.getItem(j);
            if (!itemstack.isEmpty()) {
                if (FIREWORK_ROCKET_INGREDIENT.test(itemstack)) {
                    ++fireworkCount;
                } else if (GUNPOWDER_INGREDIENT.test(itemstack)) {
                    ++gunpowederCount;
                } else {
                    return false;
                }
            }
        }

        return fireworkCount == 3 && gunpowederCount >= 1;
    }

    public ItemStack assemble(CraftingContainer container) {
        ItemStack itemstack = new ItemStack(Items.FIREWORK_ROCKET, 3);
        CompoundTag compoundtag = itemstack.getOrCreateTagElement("Fireworks");
        ItemStack inputFirework = null;
        ListTag listtag = new ListTag();
        int i = 0;

        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemstack1 = container.getItem(j);
            if (!itemstack1.isEmpty()) {
                if (GUNPOWDER_INGREDIENT.test(itemstack1)) {
                    ++i;
                } else if (FIREWORK_ROCKET_INGREDIENT.test(itemstack1)) {
                    inputFirework = itemstack1;
                }
            }
        }

        if (inputFirework != null) {
            CompoundTag inputFireworkTag = inputFirework.getTagElement("Fireworks");
            if (inputFireworkTag != null) {
                if (inputFireworkTag.contains("Flight", 99)) {
                    i += inputFireworkTag.getByte("Flight");
                }
                listtag = inputFireworkTag.getList("Explosions", 10);
            }
        }

        compoundtag.putByte("Flight", (byte)i);
        if (!listtag.isEmpty()) {
            compoundtag.put("Explosions", listtag);
        }
        return itemstack;
    }
}
