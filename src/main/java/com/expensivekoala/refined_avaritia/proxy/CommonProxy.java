package com.expensivekoala.refined_avaritia.proxy;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.Registry;
import com.expensivekoala.refined_avaritia.gui.handlers.GuiHandler;
import com.expensivekoala.refined_avaritia.network.*;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.util.PatternEventHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
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
        RefinedAvaritia.instance.network.registerMessage(MessageTransferAvaritiaRecipe.class, MessageTransferAvaritiaRecipe.class, 3, Side.SERVER);
        RefinedAvaritia.instance.network.registerMessage(MessageSetTableSize.class, MessageSetTableSize.class, 4, Side.SERVER);
    }

    public void init(FMLInitializationEvent e)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(RefinedAvaritia.instance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new PatternEventHandler());
    }

    public void postInit(FMLPostInitializationEvent e)
    {

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        if(Loader.isModLoaded("avaritia")) {
            event.getRegistry().register(Registry.EXTREME_PATTERN_ENCODER);

            GameRegistry.registerTileEntity(TileExtremePatternEncoder.class, new ResourceLocation(RefinedAvaritia.MODID,"extreme_pattern_encoder"));
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(Registry.PATTERN);
        if(Loader.isModLoaded("avaritia")) {
            event.getRegistry().register(Registry.EXTREME_PATTERN_ENCODER.createItem());
        }
    }
}

