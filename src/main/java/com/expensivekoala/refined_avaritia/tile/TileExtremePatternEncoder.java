package com.expensivekoala.refined_avaritia.tile;

import com.expensivekoala.refined_avaritia.Registry;
import com.expensivekoala.refined_avaritia.item.ItemExtremePattern;
import com.raoulvdberge.refinedstorage.inventory.ItemHandlerBase;
import com.raoulvdberge.refinedstorage.inventory.ItemValidatorBasic;
import com.raoulvdberge.refinedstorage.tile.TileBase;
import com.raoulvdberge.refinedstorage.util.StackUtils;
import morph.avaritia.recipe.AvaritiaRecipeManager;
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

    private ItemHandlerBase patterns = new ItemHandlerBase(2, new ItemValidatorBasic(Registry.PATTERN));
    private ItemHandlerBase recipe = new ItemHandlerBase(9 * 9);
    private ItemHandlerBase recipeOutput = new ItemHandlerBase(1);

    private boolean oredictPattern;

    public TileExtremePatternEncoder() {
        oredictPattern = false;
    }

    @Override
    public NBTTagCompound write(NBTTagCompound tag) {
        super.write(tag);

        StackUtils.writeItems(patterns, 0, tag);
        StackUtils.writeItems(recipe, 1, tag);

        tag.setBoolean(NBT_OREDICT_PATTERN, oredictPattern);

        return tag;
    }

    @Override
    public void read(NBTTagCompound tag) {
        super.read(tag);
        StackUtils.readItems(patterns, 0, tag);
        StackUtils.readItems(recipe, 1, tag);
        oredictPattern = tag.getBoolean(NBT_OREDICT_PATTERN);

    }

    public void onCreatePattern() {
        if(canCreatePattern()) {
            patterns.extractItem(0,1,false);

            ItemStack pattern = new ItemStack(Registry.PATTERN);

            ItemExtremePattern.setOredict(pattern, oredictPattern);

            for (int i = 0; i < 81; i++) {
                ItemStack ingredient = recipe.getStackInSlot(i);

                if(!ingredient.isEmpty()) {
                    ItemExtremePattern.setSlot(pattern, i, ingredient);
                }
            }

            patterns.setStackInSlot(1, pattern);
        }
    }

    public boolean canCreatePattern() {
        return !recipeOutput.getStackInSlot(0).isEmpty() && patterns.getStackInSlot(1).isEmpty() && !patterns.getStackInSlot(0).isEmpty();
    }

    public void onContentsChanged() {
        ItemStack stack = AvaritiaRecipeManager.getExtremeCraftingResult(getCrafting(recipe), getWorld());
        recipeOutput.setStackInSlot(0, stack);
        markDirty();
    }

    public void clearRecipe() {
        for (int i = 0; i < recipe.getSlots(); i++) {
            recipe.setStackInSlot(i, ItemStack.EMPTY);
        }
        onContentsChanged();
    }

    public boolean getOredictPattern() {
        return oredictPattern;
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
            if(recipe.getStackInSlot(i) != ItemStack.EMPTY)
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
