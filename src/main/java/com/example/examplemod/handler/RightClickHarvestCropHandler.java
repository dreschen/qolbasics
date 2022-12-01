package com.example.examplemod.handler;

import com.example.examplemod.config.QOLBasicsConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.examplemod.utils.CropUtils.*;
import static com.example.examplemod.utils.RelativePositionUtils.getRelative3x3Positions;
import static net.minecraftforge.common.PlantType.*;

public class RightClickHarvestCropHandler {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<Item> handBlocklist = Arrays.asList(Items.BONE_MEAL);
    private static final List<Block> multiBlockCrops = Arrays.asList(Blocks.SUGAR_CANE, Blocks.CACTUS);

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        if(!eventShouldContinue(player, level, event.getPos())) {
            return;
        }
        ItemStack inHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        List<BlockPos> targetCrops = player.isCrouching() ?
                Collections.singletonList(event.getPos()) : getRelative3x3Positions(event.getPos(), player.getLookAngle());
        for(BlockPos blockPos : targetCrops) {
            BlockState blockState = level.getBlockState(blockPos);
            mineAndReplace(inHand, level, blockState, blockPos, player);
        }
    }

    private static boolean eventShouldContinue(Player player, Level level, BlockPos eventPos) {
        if (player == null || level == null
                || isPlayerHoldingBlocklistedItem(player)
                || isPlayerHoldingMultiBlockCrop(player)||
                !QOLBasicsConfig.RIGHT_CLICK_HARVEST.get()) {
            return false;
        }
        BlockState targetBlockState = level.getBlockState(eventPos);
        return isPlantable(targetBlockState.getBlock());
    }
    private static boolean isPlayerHoldingBlocklistedItem(Player player) {
        if(player == null) {
            return false;
        }
        ItemStack inMainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack inOffHand = player.getItemInHand(InteractionHand.OFF_HAND);
        return (handBlocklist.contains(inMainHand.getItem()) || handBlocklist.contains(inOffHand.getItem()));
    }

    private static boolean isPlayerHoldingMultiBlockCrop(Player player) {
        if(player == null) {
            return false;
        }
        return (multiBlockCrops.contains(getBlockFromItemStack(player.getItemInHand(InteractionHand.MAIN_HAND)))
                || multiBlockCrops.contains(getBlockFromItemStack(player.getItemInHand(InteractionHand.OFF_HAND))));
    }

    private static Block getBlockFromItemStack(ItemStack itemStack) {
        if(itemStack != null && itemStack.getItem() instanceof BlockItem) {
            return ((BlockItem) itemStack.getItem()).getBlock();
        }
        return null;
    }

    private static void mineAndReplace(ItemStack inHand, Level level, BlockState blockState, BlockPos blockPos, Player player) {
        if(shouldMineBlock(blockState, level, blockPos)) {
            minePlantable(inHand, level, blockState, blockPos, player);
        }
        IPlantable plantable = getPlantable(inHand);
        if(shouldReplaceBlock(level, player, blockPos, blockState, plantable)) {
            inHand.useOn(new UseOnContext(
                    player,
                    InteractionHand.MAIN_HAND,
                    new BlockHitResult(new Vec3(0,0,0), Direction.UP, blockPos, false)));
        }
    }

    private static boolean shouldReplaceBlock(Level level, Player player, BlockPos blockPos, BlockState blockState, IPlantable plantable) {
        return (plantable != null
                && canPlaceCrop(level, blockPos, player.getDirection(), plantable)
                && QOLBasicsConfig.RIGHT_CLICK_ALLOW_CROP_REPLACEMENT.get()
                && !multiBlockCrops.contains(blockState.getBlock()));
    }

    private static void minePlantable(ItemStack inHand, Level level, BlockState blockState, BlockPos blockPos, Player player) {
        BlockState harvestBlockState = blockState;
        BlockPos harvestBlockPos = blockPos;
        if (multiBlockCrops.contains(blockState.getBlock())) {
            // if it is a multiBlock mine the one above it if its the same type.
            Block block = blockState.getBlock();
            BlockPos bottomBlockPos = blockPos;
            while(level.getBlockState(bottomBlockPos.below()).is(block)) {
                bottomBlockPos = bottomBlockPos.below();
            }
            if(level.getBlockState(bottomBlockPos.above()).is(block)) {
                harvestBlockPos = bottomBlockPos.above();
                harvestBlockState = level.getBlockState(harvestBlockPos);
                mine(inHand, level, harvestBlockState, harvestBlockPos, player);
            }
        }
        else {
            // not a multiBlock crop, harvest it
            mine(inHand, level, harvestBlockState, harvestBlockPos, player);
        }
    }

    private static void mine(ItemStack inHand, Level level, BlockState blockState, BlockPos blockPos, Player player) {
        blockState.getBlock().playerWillDestroy(level, blockPos, blockState, player);
        blockState.getBlock().playerDestroy(level, player, blockPos, blockState, null, inHand);
        level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
    }

    private static boolean shouldMineBlock(BlockState blockState, Level level, BlockPos blockPos) {
        if (blockState == null) {
            return false;
        }
        IPlantable plantable = getPlantable(blockState.getBlock());
        if(plantable == null) {
            return false;
        }
        PlantType plantType = plantable.getPlantType(level, blockPos);
        if (DESERT.equals(plantType)) {
            return true;
        } else if (BEACH.equals(plantType)) { // all (sugarcane)
            return true;
        } else if (CAVE.equals(plantType)) { // all (mushrooms)
            return true;
        } else if (NETHER.equals(plantType)) { // all (mushrooms)
            return blockState.getValue(BlockStateProperties.AGE_3) == 3;
        } else if (CROP.equals(plantType)) {
            return ((CropBlock) blockState.getBlock()).isMaxAge(blockState) && !(plantable instanceof StemBlock);
        } else if (PLAINS.equals(plantType)) { // don't break saplings or flowers
            return !(plantable instanceof FlowerBlock) && !(plantable instanceof SaplingBlock);
        }
        return false; // WATER will return false (lilyPads)
    }
}
