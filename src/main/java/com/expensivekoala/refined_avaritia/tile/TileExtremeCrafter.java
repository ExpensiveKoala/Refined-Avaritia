package com.expensivekoala.refined_avaritia.tile;

import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.tile.TileNode;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class TileExtremeCrafter extends TileNode implements ICraftingPatternContainer {
    @Override
    public int getSpeedUpdateCount()
    {
        return 0;
    }

    @Override
    public IItemHandler getFacingInventory()
    {
        return null;
    }

    @Override
    public List<ICraftingPattern> getPatterns()
    {
        return null;
    }

    @Override
    public void updateNode()
    {

    }

    @Override
    public int getEnergyUsage()
    {
        return 0;
    }
}
