package com.expensivekoala.refined_avaritia.proxy;

import com.expensivekoala.refined_avaritia.RABlocks;
import com.expensivekoala.refined_avaritia.RAItems;
import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.gui.handlers.GuiHandler;
import com.expensivekoala.refined_avaritia.network.MessageCreateExtremePattern;
import com.expensivekoala.refined_avaritia.tile.TileExtremeCrafter;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e)
    {
        RefinedAvaritia.instance.network.registerMessage(MessageCreateExtremePattern.class, MessageCreateExtremePattern.class, 0, Side.SERVER);

        NetworkRegistry.INSTANCE.registerGuiHandler(RefinedAvaritia.instance, new GuiHandler());

        GameRegistry.registerTileEntity(TileExtremeCrafter.class, RefinedAvaritia.MODID + ":extreme_crafter");
        GameRegistry.registerTileEntity(TileExtremePatternEncoder.class, RefinedAvaritia.MODID + ":extreme_pattern_encoder");

        GameRegistry.register(RABlocks.EXTREME_CRAFTER);
        GameRegistry.register(RABlocks.EXTREME_CRAFTER.createItem());

        GameRegistry.register(RABlocks.EXTREME_PATTERN_ENCODER);
        GameRegistry.register(RABlocks.EXTREME_PATTERN_ENCODER.createItem());

        GameRegistry.register(RAItems.PATTERN);
    }

    public void init(FMLInitializationEvent e)
    {

    }

    public void postInit(FMLPostInitializationEvent e)
    {

    }
}

