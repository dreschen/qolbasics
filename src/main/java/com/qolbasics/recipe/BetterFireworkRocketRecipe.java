package com.qolbasics.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.FireworkRocketRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class BetterFireworkRocketRecipe extends FireworkRocketRecipe {
    private static final Ingredient PAPER_INGREDIENT = Ingredient.of(Items.PAPER);
    private static final Ingredient GUNPOWDER_INGREDIENT = Ingredient.of(Items.GUNPOWDER);
    private static final Ingredient STAR_INGREDIENT = Ingredient.of(Items.FIREWORK_STAR);

    public BetterFireworkRocketRecipe(ResourceLocation id) {
        super(id);
    }

    public boolean matches(CraftingContainer container, Level level) {
        boolean flag = false;
        int i = 0;

        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemstack = container.getItem(j);
            if (!itemstack.isEmpty()) {
                if (PAPER_INGREDIENT.test(itemstack)) {
                    if (flag) {
                        return false;
                    }

                    flag = true;
                } else if (GUNPOWDER_INGREDIENT.test(itemstack)) {
                    ++i;
                } else if (!STAR_INGREDIENT.test(itemstack)) {
                    return false;
                }
            }
        }

        return flag && i >= 1;
    }
}
