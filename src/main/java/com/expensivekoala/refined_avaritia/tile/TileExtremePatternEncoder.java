package com.expensivekoala.refined_avaritia.tile;

import com.expensivekoala.refined_avaritia.Registry;
import com.expensivekoala.refined_avaritia.item.ItemExtremePattern;
import com.raoulvdberge.refinedstorage.RSUtils;
import com.raoulvdberge.refinedstorage.inventory.ItemHandlerBasic;
import com.raoulvdberge.refinedstorage.inventory.ItemValidatorBasic;
import com.raoulvdberge.refinedstorage.tile.TileBase;
import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileExtremePatternEncoder extends TileBase {
    private static final String NBT_OREDICT_PATTERN = "OredictPattern";

    private ItemHandlerBasic patterns = new ItemHandlerBasic(2, this, new ItemValidatorBasic(Registry.PATTERN));
    private ItemHandlerBasic recipe = new ItemHandlerBasic(9 * 9, this);
    private ItemHandlerBasic recipeOutput = new ItemHandlerBasic(1, this);

    private boolean oredictPattern;

    public TileExtremePatternEncoder() {
        oredictPattern = false;
    }

    @Override
    public NBTTagCompound write(NBTTagCompound tag) {
        super.write(tag);

        RSUtils.writeItems(patterns, 0, tag);
        RSUtils.writeItems(recipe, 1, tag);

        tag.setBoolean(NBT_OREDICT_PATTERN, oredictPattern);

        return tag;
    }

    @Override
    public void read(NBTTagCompound tag) {
        super.read(tag);
        RSUtils.readItems(patterns, 0, tag);
        RSUtils.readItems(recipe, 1, tag);
        oredictPattern = tag.getBoolean(NBT_OREDICT_PATTERN);

    }

    public void onCreatePattern() {
        if(canCreatePattern()) {
            patterns.extractItem(0,1,false);

            ItemStack pattern = new ItemStack(Registry.PATTERN);

            ItemExtremePattern.setOredict(pattern, oredictPattern);

            for (int i = 0; i < 81; i++) {
                ItemStack ingredient = recipe.getStackInSlot(i);

                if(ingredient != null) {
                    ItemExtremePattern.setSlot(pattern, i, ingredient);
                }
            }

            patterns.setStackInSlot(1, pattern);
        }
    }

    public boolean canCreatePattern() {
        return recipeOutput.getStackInSlot(0) != null && patterns.getStackInSlot(1) == null && patterns.getStackInSlot(0) != null;
    }

    public void onContentsChanged() {
        recipeOutput.setStackInSlot(0, ExtremeCraftingManager.getInstance().findMatchingRecipe(getCrafting(recipe), getWorld()));
        markDirty();
    }

    public void clearRecipe() {
        for (int i = 0; i < recipe.getSlots(); i++) {
            recipe.setStackInSlot(i, null);
        }
        onContentsChanged();
    }

    public void setOredictPattern(boolean oredictPattern) {
        this.oredictPattern = oredictPattern;
    }

    public static InventoryCrafting getCrafting(IItemHandler recipe) {
        Container craftingContainer = new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        };
        InventoryCrafting crafting = new InventoryCrafting(craftingContainer, 9, 9);

        for (int i = 0; i < recipe.getSlots(); i++) {
            crafting.setInventorySlotContents(i, recipe.getStackInSlot(i));
        }

        return crafting;
    }

    public IItemHandler getPatterns() {
        return patterns;
    }

    public IItemHandler getRecipe() {
        return recipe;
    }

    public IItemHandler getRecipeOutput() { return recipeOutput; }

    @Override
    public IItemHandler getDrops() {
        return patterns;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) patterns;
        }
        return super.getCapability(capability, facing);
    }
}
