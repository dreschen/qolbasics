package com.qolbasics.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class StoredExperience implements IStoredExperience {
    private int expTotal = 0;

    private int expLevel = 0;

    private float expProgress = 0;
    private static final String expTotalNbtKey = "storedExpTotal";
    private static final String expLevelNbtKey = "storedExpLevel";
    private static final String expProgressNbtKey = "storedExpProgress";
    public static final Capability<IStoredExperience> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    @Override
    public int getExpTotal() {
        return expTotal;
    }

    @Override
    public void setExpTotal(int value) {
        expTotal = value;
    }

    @Override
    public int getExpLevel() {
        return expLevel;
    }

    @Override
    public void setExpLevel(int value) {
        expLevel = value;
    }

    @Override
    public float getExpProgress() {
        return expProgress;
    }

    @Override
    public void setExpProgress(float value) {
        expProgress = value;
    }

    @Override
    public void copyFrom(IStoredExperience storedExperience) {
        this.expTotal = storedExperience.getExpTotal();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(expTotalNbtKey, this.expTotal);
        nbt.putInt(expLevelNbtKey, this.expLevel);
        nbt.putFloat(expProgressNbtKey, this.expProgress);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.expTotal = nbt.getInt(expTotalNbtKey);
        this.expLevel = nbt.getInt(expLevelNbtKey);
        this.expProgress= nbt.getFloat(expProgressNbtKey);
    }

}