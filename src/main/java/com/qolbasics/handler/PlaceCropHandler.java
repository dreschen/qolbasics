package com.qolbasics.handler;

import com.mojang.logging.LogUtils;
import com.qolbasics.utils.RelativePositionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import java.util.List;

import static com.qolbasics.utils.CropUtils.*;
import static java.lang.Math.abs;

public class PlaceCropHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        LOGGER.info("onRightClick");
        if (player == null || player.isCrouching() || level == null || event.getFace() != Direction.UP) {
            // TODO: Check for direction.up limits planting from the side of an object.
            //  Possible alternative could be grabbing block adjacent (in direction of face clicked)
            //  and taking that as root position for the event.
            LOGGER.info("aborting block event");
            return;
        }

        ItemStack inHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        LOGGER.info("main hand: " + inHand);
        IPlantable plantable = getPlantable(inHand);
        LOGGER.info("main hand plantable: " + plantable);
        if(plantable == null || plantable instanceof CactusBlock) {
            return;
        }
        List<BlockPos> targetPositions = RelativePositionUtils.getRelative3x3Positions(event.getPos().above(), player.getLookAngle());
        for(BlockPos blockPos : targetPositions){
            if(inHand.isEmpty()) {
                break;
            }
            if(canPlaceCrop(level, blockPos, player.getDirection(), plantable)) {
                LOGGER.info("Using inhand");
                inHand.useOn(new UseOnContext(
                        player,
                        InteractionHand.MAIN_HAND,
                        new BlockHitResult(new Vec3(0,0,0), Direction.UP, blockPos, false)));
            }
            else {
                LOGGER.info("can't use inHand here.");
            }
        }
    }
}
