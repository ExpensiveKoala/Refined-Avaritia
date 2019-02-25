package com.expensivekoala.refined_avaritia.proxy;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.Registry;
import com.expensivekoala.refined_avaritia.gui.ContainerExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.gui.handlers.GuiHandler;
import com.expensivekoala.refined_avaritia.item.ItemExtremePattern;
import com.expensivekoala.refined_avaritia.network.*;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
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

@Mod.EventBusSubscriber(modid = RefinedAvaritia.MODID)
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        int id = 0;
        RefinedAvaritia.instance.network.registerMessage(MessageCreateExtremePattern.class, MessageCreateExtremePattern.class, id++, Side.SERVER);
        RefinedAvaritia.instance.network.registerMessage(MessageClearExtremePattern.class, MessageClearExtremePattern.class, id++, Side.SERVER);
        RefinedAvaritia.instance.network.registerMessage(MessageSetOredictExtremePattern.class, MessageSetOredictExtremePattern.class, id++, Side.SERVER);
        RefinedAvaritia.instance.network.registerMessage(MessageSetAvaritiaPattern.class, MessageSetAvaritiaPattern.class, id++, Side.SERVER);
        RefinedAvaritia.instance.network.registerMessage(MessageTransferAvaritiaRecipe.class, MessageTransferAvaritiaRecipe.class, id++, Side.SERVER);
        RefinedAvaritia.instance.network.registerMessage(MessageSetTableSize.class, MessageSetTableSize.class, id++, Side.SERVER);
    }

    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(RefinedAvaritia.instance, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {
        API.instance().addPatternRenderHandler(pattern -> {
            Container container = Minecraft.getMinecraft().player.openContainer;

            if(container instanceof ContainerExtremePatternEncoder) {
                for (int i = 0; i < container.inventorySlots.size(); i++) {
                    if(pattern == container.inventorySlots.get(i).getStack() && pattern.getItem() instanceof ItemExtremePattern) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(Registry.EXTREME_PATTERN_ENCODER);

        GameRegistry.registerTileEntity(TileExtremePatternEncoder.class, new ResourceLocation(RefinedAvaritia.MODID, "extreme_pattern_encoder"));

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(Registry.PATTERN);
        event.getRegistry().register(Registry.EXTREME_PATTERN_ENCODER.createItem());

    }
}

