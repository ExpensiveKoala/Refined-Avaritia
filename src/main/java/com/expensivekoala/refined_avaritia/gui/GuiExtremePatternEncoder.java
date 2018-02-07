package com.expensivekoala.refined_avaritia.gui;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.network.MessageClearExtremePattern;
import com.expensivekoala.refined_avaritia.network.MessageCreateExtremePattern;
import com.expensivekoala.refined_avaritia.network.MessageSetOredictExtremePattern;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiExtremePatternEncoder extends GuiContainer{
    private static final Map<String, ResourceLocation> TEXTURE_CACHE = new HashMap<>();


    int screenWidth, screenHeight, lastButtonId;
    TileExtremePatternEncoder tile;
    GuiCheckBox oredictPattern;
    public GuiExtremePatternEncoder(ContainerExtremePatternEncoder container, TileExtremePatternEncoder tile) {
        super(container);
        this.screenHeight = 256;
        this.screenWidth = 238;
        this.tile = tile;
        this.xSize = screenWidth;
        this.ySize = screenHeight;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        lastButtonId = 0;

        oredictPattern = addCheckBox(guiLeft + 175, guiTop + 156, I18n.format("misc.refined_avaritia:oredict"), tile.getOredictPattern());
    }



    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        try {
            super.drawScreen(mouseX, mouseY, partialTicks);
        } catch (Exception e) {

        }

        renderHoveredToolTip(mouseX, mouseY);
    }

    public GuiCheckBox addCheckBox(int x, int y, String text, boolean checked) {
        GuiCheckBox checkBox = new GuiCheckBox(lastButtonId++, x, y, text, checked);

        buttonList.add(checkBox);

        return checkBox;
    }

    private boolean isOverCreatePattern(int mouseX, int mouseY) {
        return inBounds(200, 59, 16, 16, mouseX, mouseY) && tile.canCreatePattern();
    }

    private boolean isOverClear(int mouseX, int mouseY) {
        return inBounds(176, 8, 7, 7, mouseX, mouseY);
    }

    public boolean inBounds(int x, int y, int w, int h, int ox, int oy) {
        return ox >= x && ox <= x + w && oy >= y && oy <= y + h;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if(button == oredictPattern) {
            tile.setOredictPattern(oredictPattern.isChecked());
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
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        bindTexture(RefinedAvaritia.MODID, "gui/extreme_pattern_encoder.png");

        drawTexture(guiLeft, guiTop, 0, 0, screenWidth, screenHeight);


        int ty = 0;

        if(isOverCreatePattern(mouseX-guiLeft, mouseY-guiTop))
            ty = 1;

        if(tile != null && !tile.canCreatePattern())
            ty = 2;

        drawTexture(guiLeft + 199, guiTop + 57, 240, ty * 16, 16, 16);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        mouseX -= guiLeft;
        mouseY -= guiTop;

        if(isOverClear(mouseX, mouseY)) {
            drawTooltip(mouseX, mouseY, I18n.format("misc.refined_avaritia:clear_tooltip"));
        }

        if(isOverCreatePattern(mouseX, mouseY)) {
            drawTooltip(mouseX, mouseY, I18n.format("misc.refined_avaritia:create_pattern_tooltip"));
        }
    }

    private void bindTexture(String base, String file) {
        String id = base + ":" + file;

        if (!TEXTURE_CACHE.containsKey(id)) {
            TEXTURE_CACHE.put(id, new ResourceLocation(base, "textures/" + file));
        }

        mc.getTextureManager().bindTexture(TEXTURE_CACHE.get(id));
    }

    public void drawTooltip(@Nonnull ItemStack stack, int x, int y, String lines) {
        drawTooltip(stack, x, y, Arrays.asList(lines.split("\n")));
    }

    public void drawTooltip(int x, int y, String lines) {
        drawTooltip(ItemStack.EMPTY, x, y, lines);
    }

    public void drawTooltip(@Nonnull ItemStack stack, int x, int y, List<String> lines) {
        GlStateManager.disableLighting();
        GuiUtils.drawHoveringText(stack, lines, x, y, width - guiLeft, height, -1, fontRenderer);
        GlStateManager.enableLighting();
    }

    public void drawTexture(int x, int y, int textureX, int textureY, int width, int height) {
        drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }
}
