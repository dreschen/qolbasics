package com.qolbasics.item;

import com.qolbasics.QOLBasicsMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StoredExperienceProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final ResourceLocation IDENTIFIER = new ResourceLocation(QOLBasicsMod.MODID, "properties");

    private final IStoredExperience storedExperience = new StoredExperience();
    private final LazyOptional<IStoredExperience> optional = LazyOptional.of(() -> this.storedExperience);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return StoredExperience.INSTANCE.orEmpty(cap, this.optional);
    }

    void invalidate() {
        this.optional.invalidate();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.storedExperience.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.storedExperience.deserializeNBT(nbt);
    }
}