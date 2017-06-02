package com.expensivekoala.refined_avaritia.util;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class Configs {
    Configuration configs;

    public int extremeCrafterUsage;
    public int extremeCrafterUsagePerPattern;



    public Configs(File file) {
        configs = new Configuration(file);
        MinecraftForge.EVENT_BUS.register(this);
        loadConfigs();
    }

    public Configuration getConfig() {
        return configs;
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.getModID().equals(RefinedAvaritia.MODID)) {
            loadConfigs();
        }
    }

    public void loadConfigs() {

        extremeCrafterUsage = configs.getInt("extremeCrafterUsage", "ENERGY", 18, 0, Integer.MAX_VALUE, "Energy usage for the Extreme Crafter.");
        extremeCrafterUsagePerPattern = configs.getInt("extremeCrafterUsagePerPattern", "ENERGY", 9, 0, Integer.MAX_VALUE, "Additional energy usage for the Extreme Crafter, per pattern.");

        if(configs.hasChanged())
            configs.save();
    }
}
