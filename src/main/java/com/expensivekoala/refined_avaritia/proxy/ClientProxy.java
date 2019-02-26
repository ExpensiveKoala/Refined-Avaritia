package com.expensivekoala.refined_avaritia.proxy;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.Registry;
import com.expensivekoala.refined_avaritia.client.BakedExtremePatternModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = RefinedAvaritia.MODID)
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        //Items
        ModelLoader.setCustomModelResourceLocation(Registry.PATTERN, 0, new ModelResourceLocation(Registry.PATTERN.getRegistryName(), "inventory"));

        //ItemBlocks
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Registry.EXTREME_PATTERN_ENCODER), 0, new ModelResourceLocation(Registry.EXTREME_PATTERN_ENCODER.getRegistryName(), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelBakeEvent(ModelBakeEvent event) {
        IBakedModel existingModel =  event.getModelRegistry().getObject(new ModelResourceLocation(RefinedAvaritia.MODID + ":extreme_pattern", "inventory"));
        if(existingModel != null) {
            BakedExtremePatternModel model = new BakedExtremePatternModel(existingModel);
        event.getModelRegistry().putObject(new ModelResourceLocation(RefinedAvaritia.MODID + ":extreme_pattern", "inventory"), model);
        }
    }
}
