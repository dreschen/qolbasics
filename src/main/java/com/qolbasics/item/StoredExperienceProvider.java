package com.qolbasics.item;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class StoredExperienceProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<StoredExperience> STORED_EXPERIENCE = CapabilityManager.get(new CapabilityToken<StoredExperience>(){ });

    private StoredExperience storedExperience = null;
    private final LazyOptional<StoredExperience> optional = LazyOptional.of(this::createStoredExperience);

    private StoredExperience createStoredExperience() {
        if(this.storedExperience == null) {
            this.storedExperience = new StoredExperience();
        }

        return this.storedExperience;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == STORED_EXPERIENCE) {
            return optional.cast();
        }
        return LazyOptional.empty();

    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createStoredExperience().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createStoredExperience().loadNBTData(nbt);
    }
}