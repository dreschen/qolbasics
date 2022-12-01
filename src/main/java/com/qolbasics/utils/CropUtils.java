package com.qolbasics.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import org.slf4j.Logger;

public class CropUtils {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static boolean isPlantable(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (itemStack.getItem() instanceof BlockItem blockItem) {
            return isPlantable(blockItem.getBlock());
        }
        return false;
    }

    public static boolean isPlantable(Block block) {
        if (block == null) {
            return false;
        }
        return block instanceof IPlantable;
    }

    public static IPlantable getPlantable(ItemStack itemStack) {
        if (isPlantable(itemStack)) {
            return (IPlantable) ((BlockItem) itemStack.getItem()).getBlock();
        }
    return null;
    }


    public static IPlantable getPlantable(Block block) {
        if (isPlantable(block)) {
            return (IPlantable) block;
        }
        return null;
    }

    public static boolean targetBlockAboveAppropriateFarmland(BlockPos blockPos, Level level, Direction facing, IPlantable plantable) {
        BlockState blockState = level.getBlockState(blockPos.below());
        if(plantable instanceof SugarCaneBlock sugarCaneBlock) {
            return sugarCaneBlock.canSurvive(blockState, level, blockPos);
        }
        return blockState.getBlock().canSustainPlant(blockState, level, blockPos, facing, plantable);
    }

    public static boolean canPlaceCrop(Level level, BlockPos blockPos, Direction facing, IPlantable crop) {
        boolean isBlockAir = level.getBlockState(blockPos).getBlock() instanceof AirBlock;
        boolean isBlockAboveAppropriateFarmland = targetBlockAboveAppropriateFarmland(blockPos, level, facing, crop);
        LOGGER.info("isBlockAir: " + isBlockAir);
        LOGGER.info("isBlockAboveAppropriateFarmland: " + isBlockAboveAppropriateFarmland);
        return  isBlockAboveAppropriateFarmland && isBlockAir;
    }
}
