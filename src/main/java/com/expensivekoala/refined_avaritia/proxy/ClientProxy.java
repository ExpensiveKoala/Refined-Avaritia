package com.expensivekoala.refined_avaritia.proxy;

import com.expensivekoala.refined_avaritia.Registry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        //Items
        ModelLoader.setCustomModelResourceLocation(Registry.PATTERN, 0, new ModelResourceLocation(Registry.PATTERN.getRegistryName(), "inventory"));

        //ItemBlocks
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Registry.EXTREME_PATTERN_ENCODER), 0, new ModelResourceLocation(Registry.EXTREME_PATTERN_ENCODER.getRegistryName(), "inventory"));

    }
}
