package com.expensivekoala.refined_avaritia.util;

import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author ExpensiveKoala
 */
public class ItemHandlerPhantom extends ItemStackHandler {

    TileExtremePatternEncoder encoder;
    boolean result;

    public ItemHandlerPhantom(int size, TileExtremePatternEncoder tile, boolean result) {
        super(size); //Super-size me!
        encoder = tile;
        this.result = result;
    }

    @Override
    protected int getStackLimit(int slot, ItemStack stack) {
        return 1;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if(!result)
            setStackInSlot(slot, stack);
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        setStackInSlot(slot, null);
        return null;
    }

    @Override
    protected void onContentsChanged(int slot) {
        encoder.onContentsChanged();
    }
}
