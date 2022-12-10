package com.qolbasics.item;

import net.minecraft.nbt.CompoundTag;

public class StoredExperience {
    private int expAmount;

    public int getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(int value) {
        expAmount = value;
    }

    public void copyFrom(StoredExperience storedExperience) {
        this.expAmount = storedExperience.getExpAmount();
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("storedExp", expAmount);
    }

    public void loadNBTData(CompoundTag nbt) {
        expAmount = nbt.getInt("storedExp");
    }

}