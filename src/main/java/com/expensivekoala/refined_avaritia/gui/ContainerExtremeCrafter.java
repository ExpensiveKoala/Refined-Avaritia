package com.expensivekoala.refined_avaritia.gui;

import com.expensivekoala.refined_avaritia.tile.TileExtremeCrafter;
import com.raoulvdberge.refinedstorage.container.ContainerBase;
import com.raoulvdberge.refinedstorage.container.slot.SlotSpecimen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerExtremeCrafter extends ContainerBase {

    public ContainerExtremeCrafter(TileExtremeCrafter crafter, EntityPlayer player) {
        super(crafter, player);
        for (int i = 0; i < 4; i++)
            addSlotToContainer(new SlotItemHandler(crafter.getUpgrades(), i, 187, 6 + i * 18));

        for(int i = 0; i < 9; i++)
            addSlotToContainer(new SlotItemHandler(crafter.getPatternItems(), i, 8 + 18 * i, 20));

        addPlayerInventory(8, 55);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
        ItemStack stack = null;
        Slot slot = getSlot(index);
        if(slot != null && slot.getHasStack())
        {
            stack = slot.getStack();
            if(index < 4)
            {
                if(!mergeItemStack(stack, 13, inventorySlots.size(), false))
                    return null;
            } else
            if(!mergeItemStack(stack, 0, 4, false))
                return mergeItemStackToSpecimen(stack, 4, 13);
            if(stack.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();
        }
        return stack;
    }
}
