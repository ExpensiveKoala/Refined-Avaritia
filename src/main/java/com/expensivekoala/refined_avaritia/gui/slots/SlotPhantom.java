package com.expensivekoala.refined_avaritia.gui.slots;

import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * @author ExpensiveKoala
 */
public class SlotPhantom extends SlotItemHandler {
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
        if (!output && !stack.isEmpty()) stack.setCount(1);
        super.putStack(stack);
    }

    @Override
    public void onSlotChanged() {
        if (!output) {
            tile.onContentsChanged();
        }
    }

    public boolean isOutput() {
        return output;
    }

    @Override
    public boolean isEnabled() {
        if (output) return true;
        int x = getSlotIndex() % 9;
        int y = getSlotIndex() / 9;
        switch (tile.getTableSize()) {
            case BASIC:
                return x > 2 && x <= 5 && y > 2 && y <= 5;
            case ADVANCED:
                return x > 1 && x <= 6 && y > 1 && y <= 6;
            case ELITE:
                return x > 0 && x <= 7 && y > 0 && y <= 7;
            case ULTIMATE:
                return true;
        }
        return false;
    }
}
