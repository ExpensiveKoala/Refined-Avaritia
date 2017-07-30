package com.expensivekoala.refined_avaritia.gui;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.network.MessageClearExtremePattern;
import com.expensivekoala.refined_avaritia.network.MessageCreateExtremePattern;
import com.expensivekoala.refined_avaritia.network.MessageSetOredictExtremePattern;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import com.raoulvdberge.refinedstorage.gui.GuiBase;
import com.raoulvdberge.refinedstorage.tile.data.TileDataManager;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.client.config.GuiCheckBox;

import java.io.IOException;

public class GuiExtremePatternEncoder extends GuiBase{

    TileExtremePatternEncoder tile;
    GuiCheckBox oredictPattern;
    public GuiExtremePatternEncoder(ContainerExtremePatternEncoder container, TileExtremePatternEncoder tile) {
        super(container, 238, 256);
        this.tile = tile;
    }

    @Override
    public void init(int x, int y) {
        oredictPattern = addCheckBox(x + 175, y + 156, t("misc.refined_avaritia:oredict"), false);
    }

    @Override
    public void update(int x, int y) {

    }

    private boolean isOverCreatePattern(int mouseX, int mouseY) {
        return inBounds(200, 59, 16, 16, mouseX, mouseY) && tile.canCreatePattern();
    }

    private boolean isOverClear(int mouseX, int mouseY) {
        return inBounds(176, 8, 7, 7, mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if(button == oredictPattern) {
            RefinedAvaritia.instance.network.sendToServer(new MessageSetOredictExtremePattern(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), oredictPattern.isChecked()));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(isOverClear(mouseX - guiLeft, mouseY - guiTop)) {
            RefinedAvaritia.instance.network.sendToServer(new MessageClearExtremePattern(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ()));

            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        } else if(isOverCreatePattern(mouseX - guiLeft, mouseY - guiTop)) {
            RefinedAvaritia.instance.network.sendToServer(new MessageCreateExtremePattern(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ()));

            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public void drawBackground(int x, int y, int mouseX, int mouseY) {
        bindTexture(RefinedAvaritia.MODID, "gui/extreme_pattern_encoder.png");
        drawTexture(x, y, 0, 0, screenWidth, screenHeight);

        int ty = 0;

        if(isOverCreatePattern(mouseX-guiLeft, mouseY-guiTop))
            ty = 1;

        if(tile != null && !tile.canCreatePattern())
            ty = 2;

        drawTexture(x + 199, y + 57, 240, ty * 16, 16, 16);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        if(isOverClear(mouseX, mouseY)) {
            drawTooltip(mouseX, mouseY, t("misc.refined_avaritia:clear_tooltip"));
        }

        if(isOverCreatePattern(mouseX, mouseY)) {
            drawTooltip(mouseX, mouseY, t("misc.refined_avaritia:create_pattern_tooltip"));
        }
    }

    public void updateOredictPattern(boolean checked) {
        if(oredictPattern != null) {
            oredictPattern.setIsChecked(checked);
            tile.setOredictPattern(checked);
        }
    }
}
