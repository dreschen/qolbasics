package com.example.examplemod.config;


import net.minecraftforge.common.ForgeConfigSpec;

public class QOLBasicsConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> FEATHER_FALL_AVOIDS_TRAMPLE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RIGHT_CLICK_HARVEST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RIGHT_CLICK_ALLOW_CROP_REPLACEMENT;

    static {
        BUILDER.push("Configs for QOL Basics Mod");

        FEATHER_FALL_AVOIDS_TRAMPLE = BUILDER.comment("Feather Falling Boots Prevent Crop Trampling when 'true'")
                .define("Feather Fall Avoids Trample", true);

        RIGHT_CLICK_HARVEST = BUILDER.comment("Allows right-click harvesting of crops when 'true'")
                .define("Right-click Harvests Crops", true);

        RIGHT_CLICK_ALLOW_CROP_REPLACEMENT = BUILDER.comment("Allows crop replacement after right-click harvesting when 'true' (Player must be holding replacement seeds in primary hand)")
                .define("Allow Replacement after Right-click Harvesting", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
