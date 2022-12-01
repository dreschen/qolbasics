package com.qolbasics.utils;

import com.qolbasics.enums.CardinalDirection;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class RelativePositionUtils {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static List<BlockPos> getRelative3x3Positions(BlockPos centerPos, Vec3 lookAngle) {
        ArrayList<BlockPos> targetPositions = new ArrayList<>();

        BlockPos center = new BlockPos(centerPos.getX(), centerPos.getY(), centerPos.getZ());
        BlockPos north = new BlockPos(centerPos.getX(), centerPos.getY(), centerPos.getZ() - 1);
        BlockPos east = new BlockPos(centerPos.getX() + 1, centerPos.getY(), centerPos.getZ());
        BlockPos south = new BlockPos(centerPos.getX(), centerPos.getY(), centerPos.getZ() + 1);
        BlockPos west = new BlockPos(centerPos.getX() - 1, centerPos.getY(), centerPos.getZ());
        BlockPos northwest = new BlockPos(centerPos.getX() - 1, centerPos.getY(), centerPos.getZ() - 1);
        BlockPos northeast = new BlockPos(centerPos.getX() + 1, centerPos.getY(), centerPos.getZ() - 1);
        BlockPos southeast = new BlockPos(centerPos.getX() + 1, centerPos.getY(), centerPos.getZ() + 1);
        BlockPos southwest = new BlockPos(centerPos.getX() - 1, centerPos.getY(), centerPos.getZ() + 1);

        ArrayList<BlockPos> primaryPos = new ArrayList<>(Arrays.asList(north, south, east, west));
        ArrayList<BlockPos> secondaryPos = new ArrayList<>(Arrays.asList(northwest, southeast, northeast, southwest));

        List<Integer> primaryIdxs = angleToPrimaryIdxs(lookAngle);
        List<Integer> secondaryIdxs = angleToSecondaryIdxs(lookAngle);

        targetPositions.add(center);
        for (Integer primaryIdx : primaryIdxs) {
            targetPositions.add(primaryPos.get(primaryIdx));
        }

        for (Integer secondaryIdx : secondaryIdxs) {
            targetPositions.add(secondaryPos.get(secondaryIdx));
        }
        return targetPositions;
    }

    private static List<Integer> angleToPrimaryIdxs(Vec3 lookAngle) {
        switch (getPrimaryCardinalDirection(lookAngle)) {
            case SOUTH -> {
                return Arrays.asList(1, 2, 3, 0);
            }
            case EAST -> {
                return Arrays.asList(2, 0, 1, 3);
            }
            case WEST -> {
                return Arrays.asList(3, 1, 0, 2);
            }
            default -> { // default to North
                return Arrays.asList(0, 3, 2, 1);
            }
        }
    }

    private static List<Integer> angleToSecondaryIdxs(Vec3 lookAngle) {
        switch (getPrimaryCardinalDirection(lookAngle)) {
            case SOUTH -> {
                return Arrays.asList(1, 3, 2, 0);
            }
            case EAST -> {
                return Arrays.asList(2, 1, 0, 3);
            }
            case WEST -> {
                return Arrays.asList(3, 0, 1 ,2);
            }
            default -> { // default to North
                return Arrays.asList(0, 2, 3, 1);
            }
        }
    }

    public static CardinalDirection getPrimaryCardinalDirection(Vec3 lookAngle) {
        double x = lookAngle.x;
        double z = lookAngle.z;

        if(abs(x) >= abs(z)) {
            if(x >= 0) { //east
                return CardinalDirection.EAST;
            }
            //west
            return CardinalDirection.WEST;
        }
        if(z >= 0) { //south
            return CardinalDirection.SOUTH;
        }
        //north
        return CardinalDirection.NORTH;
    }

    public static CardinalDirection getPrimaryCardinalDirection(Entity entity) {
        switch (entity.getDirection()) {
            case NORTH -> {
                return CardinalDirection.NORTH;
            }
            case SOUTH -> {
                return CardinalDirection.SOUTH;
            }
            case EAST -> {
                return CardinalDirection.EAST;
            }
            case WEST -> {
                return CardinalDirection.WEST;
            }
        }
        // if getDirection() returns UP or DOWN, pass onto lookAngle-based function.
        return getPrimaryCardinalDirection(entity.getLookAngle());
    }
}
