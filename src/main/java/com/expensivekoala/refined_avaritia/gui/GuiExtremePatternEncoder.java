package com.expensivekoala.refined_avaritia.gui;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import com.raoulvdberge.refinedstorage.gui.GuiBase;

public class GuiExtremePatternEncoder extends GuiBase{

    TileExtremePatternEncoder tile;

    public GuiExtremePatternEncoder(ContainerExtremePatternEncoder container, TileExtremePatternEncoder tile) {
        super(container, 211, 137);
        this.tile = tile;
    }

    @Override
    public void init(int x, int y) {
        //Add buttons?
    }

    @Override
    public void update(int x, int y) {

    }

    @Override
    public void drawBackground(int x, int y, int mouseX, int mouseY) {
        bindTexture(RefinedAvaritia.MODID, "gui/extreme_pattern_encoder.png");
        drawTexture(x, y, 0, 0, screenWidth, screenHeight);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        drawString(7, 7, t("gui.refinedexchange:emcSolidifier", new Object[0]));
        drawString(7, 43, t("container.inventory", new Object[0]));
    }
}
