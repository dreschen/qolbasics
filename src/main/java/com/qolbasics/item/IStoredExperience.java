package com.qolbasics.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IStoredExperience extends INBTSerializable<CompoundTag> {
    public int getExpAmount();

    public void setExpAmount(int value);

    public void copyFrom(IStoredExperience storedExperience);

}