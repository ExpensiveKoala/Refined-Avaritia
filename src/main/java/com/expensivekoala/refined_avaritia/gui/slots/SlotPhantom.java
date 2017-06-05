package com.expensivekoala.refined_avaritia.gui.slots;

import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * @author ExpensiveKoala
 */
public class SlotPhantom extends SlotItemHandler{
    boolean output;
    TileExtremePatternEncoder tile;
    public SlotPhantom(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean output, TileExtremePatternEncoder tile) {
        super(itemHandler, index, xPosition, yPosition);
        this.output = output;
        this.tile = tile;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return false;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !output || super.isItemValid(stack);
    }

    @Override
    public void putStack(ItemStack stack) {
        if(!output && stack != null) stack.stackSize = 1;
        super.putStack(stack);
    }

    @Override
    public void onSlotChanged() {
        if(!output) {
            tile.onContentsChanged();
        }
    }

    public boolean isOutput() {
        return output;
    }
}
