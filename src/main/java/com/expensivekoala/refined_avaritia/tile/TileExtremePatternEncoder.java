package com.expensivekoala.refined_avaritia.tile;

import com.expensivekoala.refined_avaritia.RAItems;
import com.expensivekoala.refined_avaritia.gui.GuiExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.item.ItemExtremePattern;
import com.expensivekoala.refined_avaritia.util.ItemHandlerPhantom;
import com.raoulvdberge.refinedstorage.RSUtils;
import com.raoulvdberge.refinedstorage.inventory.ItemHandlerBasic;
import com.raoulvdberge.refinedstorage.inventory.ItemValidatorBasic;
import com.raoulvdberge.refinedstorage.tile.TileBase;
import com.raoulvdberge.refinedstorage.tile.data.ITileDataConsumer;
import com.raoulvdberge.refinedstorage.tile.data.ITileDataProducer;
import com.raoulvdberge.refinedstorage.tile.data.TileDataParameter;
import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileExtremePatternEncoder extends TileBase {
    private static final String NBT_OREDICT_PATTERN = "OredictPattern";

    public static final TileDataParameter<Boolean> OREDICT_PATTERN = new TileDataParameter<>(DataSerializers.BOOLEAN, false, new ITileDataProducer<Boolean, TileExtremePatternEncoder>() {
        @Override
        public Boolean getValue(TileExtremePatternEncoder tile) {
            return tile.oredictPattern;
        }
    }, new ITileDataConsumer<Boolean, TileExtremePatternEncoder>() {
        @Override
        public void setValue(TileExtremePatternEncoder tile, Boolean value) {
            tile.oredictPattern = value;

            tile.markDirty();
        }
    }, parameter -> {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiExtremePatternEncoder) {
            ((GuiExtremePatternEncoder) Minecraft.getMinecraft().currentScreen).updateOredictPattern(parameter.getValue());
        }
    });

    private ItemHandlerBasic patterns = new ItemHandlerBasic(2, this, new ItemValidatorBasic(RAItems.PATTERN));
    private ItemHandlerPhantom recipe = new ItemHandlerPhantom(9 * 9, this, false);
    private ItemHandlerPhantom recipeOutput = new ItemHandlerPhantom(1, this, true);

    private boolean oredictPattern;

    public TileExtremePatternEncoder() {
        dataManager.addWatchedParameter(OREDICT_PATTERN);
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
        RSUtils.writeItems(recipe, 1, tag);
        if(tag.hasKey(NBT_OREDICT_PATTERN)) {
            oredictPattern = tag.getBoolean(NBT_OREDICT_PATTERN);
        }
    }

    public void onCreatePattern() {
        if(canCreatePattern()) {
            patterns.extractItem(0,1,false);

            ItemStack pattern = new ItemStack(RAItems.PATTERN);

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
