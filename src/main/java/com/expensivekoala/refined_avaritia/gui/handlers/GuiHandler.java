package com.expensivekoala.refined_avaritia.gui.handlers;

import com.expensivekoala.refined_avaritia.gui.ContainerExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.gui.GuiExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int EXTREME_PATTERN_ENCODER_GUI = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == EXTREME_PATTERN_ENCODER_GUI)
            return new ContainerExtremePatternEncoder((TileExtremePatternEncoder)world.getTileEntity(new BlockPos(x, y, z)), player);
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == EXTREME_PATTERN_ENCODER_GUI) {
            return new GuiExtremePatternEncoder(new ContainerExtremePatternEncoder((TileExtremePatternEncoder) world.getTileEntity(new BlockPos(x, y, z)), player),
                    (TileExtremePatternEncoder) world.getTileEntity(new BlockPos(x, y, z)));
        }
        return null;
    }
}
