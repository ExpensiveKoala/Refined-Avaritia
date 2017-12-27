package com.expensivekoala.refined_avaritia.jei;

import com.expensivekoala.refined_avaritia.gui.GuiExtremePatternEncoder;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import morph.avaritia.compat.jei.AvaritiaJEIPlugin;

/**
 * @author ExpensiveKoala
 */
@JEIPlugin
public class RefinedAvaritiaJEI implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(new EncoderRecipeTransferHandler(), AvaritiaJEIPlugin.EXTREME_CRAFTING);
        registry.addRecipeClickArea(GuiExtremePatternEncoder.class, 175, 119, 24, 20, AvaritiaJEIPlugin.EXTREME_CRAFTING);
    }
}
