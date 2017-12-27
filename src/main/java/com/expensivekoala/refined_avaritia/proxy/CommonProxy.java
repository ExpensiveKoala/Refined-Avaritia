package com.expensivekoala.refined_avaritia.proxy;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.Registry;
import com.expensivekoala.refined_avaritia.gui.handlers.GuiHandler;
import com.expensivekoala.refined_avaritia.network.MessageClearExtremePattern;
import com.expensivekoala.refined_avaritia.network.MessageCreateExtremePattern;
import com.expensivekoala.refined_avaritia.network.MessageSetOredictExtremePattern;
import com.expensivekoala.refined_avaritia.network.MessageTransferRecipe;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e)
    {
        RefinedAvaritia.instance.network.registerMessage(MessageCreateExtremePattern.class, MessageCreateExtremePattern.class, 0, Side.SERVER);
        RefinedAvaritia.instance.network.registerMessage(MessageClearExtremePattern.class, MessageClearExtremePattern.class, 1, Side.SERVER);
        RefinedAvaritia.instance.network.registerMessage(MessageSetOredictExtremePattern.class, MessageSetOredictExtremePattern.class, 2, Side.SERVER);
        RefinedAvaritia.instance.network.registerMessage(MessageTransferRecipe.class, MessageTransferRecipe.class, 3, Side.SERVER);
    }

    public void init(FMLInitializationEvent e)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(RefinedAvaritia.instance, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent e)
    {

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(Registry.EXTREME_PATTERN_ENCODER);

        GameRegistry.registerTileEntity(TileExtremePatternEncoder.class, RefinedAvaritia.MODID + ":extreme_pattern_encoder");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(Registry.PATTERN);
        event.getRegistry().register(Registry.EXTREME_PATTERN_ENCODER.createItem());
    }
}

