package com.qolbasics.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IStoredExperience extends INBTSerializable<CompoundTag> {
    public int getExpTotal();

    public void setExpTotal(int value);

    public int getExpLevel();

    public void setExpLevel(int value);

    public float getExpProgress();

    public void setExpProgress(float value);

    public void copyFrom(IStoredExperience storedExperience);

}