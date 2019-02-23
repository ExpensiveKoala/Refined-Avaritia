package com.expensivekoala.refined_avaritia.tile;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.Registry;
import com.expensivekoala.refined_avaritia.inventory.ItemHandlerRestricted;
import com.expensivekoala.refined_avaritia.item.ItemExtremePattern;
import com.expensivekoala.refined_avaritia.util.ExtendedCraftingUtil;
import com.expensivekoala.refined_avaritia.util.ExtendedCraftingUtil.TableSize;
import com.expensivekoala.refined_avaritia.util.RecipeManager;
import com.expensivekoala.refined_avaritia.util.RecipeType;
import com.expensivekoala.refined_avaritia.util.StackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileExtremePatternEncoder extends TileEntity {
    private static final String NBT_OREDICT_PATTERN = "OredictPattern";
    private static final String NBT_TABLE_SIZE = "TableSize";

    private ItemHandlerRestricted patterns = new ItemHandlerRestricted(2, new ResourceLocation(RefinedAvaritia.MODID, "extreme_pattern"));
    private ItemHandlerRestricted recipe = new ItemHandlerRestricted(9 * 9);
    private ItemHandlerRestricted recipeOutput = new ItemHandlerRestricted(1);

    private boolean oredictPattern;
    private TableSize tableSize;

    public TileExtremePatternEncoder() {
        oredictPattern = false;
        tableSize = TableSize.ULTIMATE;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        oredictPattern = tag.getBoolean(NBT_OREDICT_PATTERN);
        tableSize = TableSize.values()[tag.getInteger(NBT_TABLE_SIZE)];
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        tag.setBoolean(NBT_OREDICT_PATTERN, oredictPattern);
        tag.setInteger(NBT_TABLE_SIZE, tableSize.ordinal());
        return tag;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        StackUtils.writeItems(patterns, 0, tag);
        StackUtils.writeItems(recipe, 1, tag);

        tag.setBoolean(NBT_OREDICT_PATTERN, oredictPattern);
        tag.setInteger(NBT_TABLE_SIZE, tableSize.ordinal());

        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        StackUtils.readItems(patterns, 0, tag);
        StackUtils.readItems(recipe, 1, tag);
        oredictPattern = tag.getBoolean(NBT_OREDICT_PATTERN);
        tableSize = TableSize.values()[tag.getInteger(NBT_TABLE_SIZE)];
    }

    public void onCreatePattern() {
        if (canCreatePattern()) {
            patterns.extractItem(0, 1, false);

            ItemStack pattern = new ItemStack(Registry.PATTERN);

            ItemExtremePattern.setOredict(pattern, oredictPattern);

            ItemExtremePattern.setType(pattern, RecipeType.AVARITIA);

            for (int i = 0; i < 81; i++) {
                ItemStack ingredient = recipe.getStackInSlot(i);

                if (!ingredient.isEmpty()) {
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
        ItemStack stack = RecipeManager.getCraftingResult(getCrafting(recipe), getWorld());
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

    public TableSize getTableSize() {
        return tableSize;
    }

    public void setTableSize(TableSize tableSize) {
        this.tableSize = tableSize;
        for (int i = 0; i < recipe.getSlots(); i++) {
            if (recipe.getStackInSlot(i) != ItemStack.EMPTY) {
                int x = i % 9;
                int y = i / 9;
                switch (this.tableSize) {
                    case BASIC:
                        if (x >= 2 && x <= 6 && (y == 2 || y == 6)) {
                            recipe.setStackInSlot(i, ItemStack.EMPTY);
                        } else if (y > 2 && y < 6 && (x == 2 || x == 6)) {
                            recipe.setStackInSlot(i, ItemStack.EMPTY);
                        }
                    case ADVANCED:
                        if (x >= 1 && x <= 7 && (y == 1 || y == 7)) {
                            recipe.setStackInSlot(i, ItemStack.EMPTY);
                        } else if (y > 1 && y < 7 && (x == 1 || x == 7)) {
                            recipe.setStackInSlot(i, ItemStack.EMPTY);
                        }
                    case ELITE:
                        if (x >= 0 && x <= 8 && (y == 0 || y == 8)) {
                            recipe.setStackInSlot(i, ItemStack.EMPTY);
                        } else if (y > 0 && y < 8 && (x == 0 || x == 8)) {
                            recipe.setStackInSlot(i, ItemStack.EMPTY);
                        }
                }
            }
        }
        onContentsChanged();
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
            if (recipe.getStackInSlot(i) != ItemStack.EMPTY)
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

    public IItemHandler getRecipeOutput() {
        return recipeOutput;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) patterns;
        }
        return super.getCapability(capability, facing);
    }
}
