package com.expensivekoala.refined_avaritia.proxy;

import com.expensivekoala.refined_avaritia.RABlocks;
import com.expensivekoala.refined_avaritia.RAItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        //Items
        ModelLoader.setCustomModelResourceLocation(RAItems.PATTERN, 0, new ModelResourceLocation("refined_avaritia:pattern", "inventory"));

        //ItemBlocks
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(RABlocks.EXTREME_PATTERN_ENCODER), 0, new ModelResourceLocation("refined_avaritia:extreme_pattern_encoder", "inventory"));
    }
}
