package com.expensivekoala.refined_avaritia.proxy;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.Registry;
import com.expensivekoala.refined_avaritia.gui.handlers.GuiHandler;
import com.expensivekoala.refined_avaritia.network.MessageClearExtremePattern;
import com.expensivekoala.refined_avaritia.network.MessageCreateExtremePattern;
import com.expensivekoala.refined_avaritia.network.MessageSetOredictExtremePattern;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
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
        RefinedAvaritia.instance.network.registerMessage(MessageClearExtremePattern.class, MessageClearExtremePattern.class, 1, Side.SERVER);
        RefinedAvaritia.instance.network.registerMessage(MessageSetOredictExtremePattern.class, MessageSetOredictExtremePattern.class, 2, Side.SERVER);

        NetworkRegistry.INSTANCE.registerGuiHandler(RefinedAvaritia.instance, new GuiHandler());

        GameRegistry.registerTileEntity(TileExtremePatternEncoder.class, RefinedAvaritia.MODID + ":extreme_pattern_encoder");

        GameRegistry.register(Registry.EXTREME_PATTERN_ENCODER);
        GameRegistry.register(Registry.EXTREME_PATTERN_ENCODER.createItem());

        GameRegistry.register(Registry.PATTERN);
    }

    public void init(FMLInitializationEvent e)
    {
        Registry.initRecipes();
    }

    public void postInit(FMLPostInitializationEvent e)
    {

    }
}

