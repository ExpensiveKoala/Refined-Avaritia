package com.expensivekoala.refined_avaritia.gui;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.tile.TileExtremeCrafter;
import com.raoulvdberge.refinedstorage.gui.GuiBase;

public class GuiExtremeCrafter extends GuiBase{

    TileExtremeCrafter tile;
    public GuiExtremeCrafter(ContainerExtremeCrafter container, TileExtremeCrafter tile) {
        super(container, 211, 137);
        this.tile = tile;
    }

    @Override
    public void init(int x, int y) {

    }

    @Override
    public void update(int x, int y) {

    }

    @Override
    public void drawBackground(int x, int y, int mouseX, int mouseY) {
        //bindTexture(RefinedAvaritia.MODID, "gui/extreme_crafter.png");
        bindTexture("gui/crafter.png");
        drawTexture(x, y, 0, 0, screenWidth, screenHeight);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        drawString(7, 7, t("gui.refined_avaritia:extreme_crafter", new Object[0]));
        drawString(7, 43, t("container.inventory", new Object[0]));
    }
}
