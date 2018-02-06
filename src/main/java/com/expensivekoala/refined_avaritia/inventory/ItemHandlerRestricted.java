package com.expensivekoala.refined_avaritia.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemHandlerRestricted extends ItemStackHandler {
    private ResourceLocation[] validInputs;

    public ItemHandlerRestricted(int size, ResourceLocation ... validInputs) {
        super(size);
        this.validInputs = validInputs;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(validInputs.length > 0) {
            for(ResourceLocation registryName: validInputs) {
                if(registryName.equals(stack.getItem().getRegistryName())) {
                    return super.insertItem(slot, stack, simulate);
                }
            }
            return stack;
        }
        return super.insertItem(slot, stack, simulate);
    }
}
