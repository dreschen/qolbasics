package com.qolbasics.recipe;

import com.qolbasics.QOLBasicsMod;
import net.minecraft.world.item.crafting.FireworkRocketRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipies {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, QOLBasicsMod.MODID);

    public static final RegistryObject<RecipeSerializer<BetterFireworkRocketRecipe>> BETTER_FIREWORK_SERIALIZER =
            SERIALIZERS.register("better_firework", () -> new SimpleRecipeSerializer<>(BetterFireworkRocketRecipe::new));

    public static final RegistryObject<RecipeSerializer<ModularFireworkRocketRecipe>> MODULAR_FIREWORK_SERIALIZER =
            SERIALIZERS.register("modular_firework", () -> new SimpleRecipeSerializer<>(ModularFireworkRocketRecipe::new));

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
