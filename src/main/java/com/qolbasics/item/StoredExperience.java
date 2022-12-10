package com.qolbasics.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class StoredExperience implements IStoredExperience {
    private int expAmount = 0;
    private static final String nbtKey = "storedExp";
    public static final Capability<IStoredExperience> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    @Override
    public int getExpAmount() {
        return expAmount;
    }

    @Override
    public void setExpAmount(int value) {
        expAmount = value;
    }

    @Override
    public void copyFrom(IStoredExperience storedExperience) {
        this.expAmount = storedExperience.getExpAmount();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(nbtKey, this.expAmount);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.expAmount = nbt.getInt(nbtKey);
    }

}