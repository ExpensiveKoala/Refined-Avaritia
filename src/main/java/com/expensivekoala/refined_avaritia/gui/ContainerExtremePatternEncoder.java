package com.expensivekoala.refined_avaritia.gui;

import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import com.raoulvdberge.refinedstorage.container.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerExtremePatternEncoder extends ContainerBase {

    public ContainerExtremePatternEncoder(TileExtremePatternEncoder tile, EntityPlayer player) {
        super(tile, player);
        addPlayerInventory(0,0);
    }
}
