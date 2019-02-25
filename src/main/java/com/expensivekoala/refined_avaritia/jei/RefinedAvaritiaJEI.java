package com.expensivekoala.refined_avaritia.jei;

import com.expensivekoala.refined_avaritia.gui.GuiExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.util.AvaritiaRecipeManagerWrapper;
import com.expensivekoala.refined_avaritia.util.ECRecipeManagerWrapper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import morph.avaritia.compat.jei.AvaritiaJEIPlugin;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author ExpensiveKoala
 */
@JEIPlugin
public class RefinedAvaritiaJEI implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        if (Loader.isModLoaded("avaritia")) {
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(new AvaritiaEncoderRecipeTransferHandler(), AvaritiaRecipeManagerWrapper.EXTREME_CRAFTING);
        }
        if (Loader.isModLoaded("extendedcrafting")) {
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(new ECBasicEncoderRecipeTransferHandler(), ECRecipeManagerWrapper.EXTREME_CRAFTING[0]);
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(new ECAdvancedEncoderRecipeTransferHandler(), ECRecipeManagerWrapper.EXTREME_CRAFTING[1]);
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(new ECEliteEncoderRecipeTransferHandler(), ECRecipeManagerWrapper.EXTREME_CRAFTING[2]);
            registry.getRecipeTransferRegistry().addRecipeTransferHandler(new ECUltimateEncoderRecipeTransferHandler(), ECRecipeManagerWrapper.EXTREME_CRAFTING[3]);
        }
        registry.addRecipeClickArea(GuiExtremePatternEncoder.class, 175, 119, 24, 20, getRecipeCategories());
    }

    private String[] getRecipeCategories() {
        String[] categories = new String[0];
        if (Loader.isModLoaded("avaritia")) {
            categories = ArrayUtils.add(categories, AvaritiaRecipeManagerWrapper.EXTREME_CRAFTING);
        }
        if (Loader.isModLoaded("extendedcrafting")) {
            categories = ArrayUtils.addAll(categories, ECRecipeManagerWrapper.EXTREME_CRAFTING);
        }
        return categories;
    }
}
