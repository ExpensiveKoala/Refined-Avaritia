package com.expensivekoala.refined_avaritia.tile;

import com.expensivekoala.refined_avaritia.RAItems;
import com.expensivekoala.refined_avaritia.gui.GuiExtremePatternEncoder;
import com.raoulvdberge.refinedstorage.RSUtils;
import com.raoulvdberge.refinedstorage.inventory.ItemHandlerBasic;
import com.raoulvdberge.refinedstorage.inventory.ItemValidatorBasic;
import com.raoulvdberge.refinedstorage.tile.TileBase;
import com.raoulvdberge.refinedstorage.tile.data.ITileDataConsumer;
import com.raoulvdberge.refinedstorage.tile.data.ITileDataProducer;
import com.raoulvdberge.refinedstorage.tile.data.TileDataParameter;
import net.minecraft.client.Minecraft;
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
    private ItemHandlerBasic recipe = new ItemHandlerBasic(9 * 9, this);

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

    }

    public boolean canCreatePattern() {
        return true;
    }

    public IItemHandler getPatterns() {
        return patterns;
    }

    public IItemHandler getRecipe() {
        return recipe;
    }

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
            return (T) patterns
        }
        return super.getCapability(capability, facing);
    }
}
