package com.expensivekoala.refined_avaritia.gui;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.gui.slots.SlotPhantom;
import com.expensivekoala.refined_avaritia.network.*;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.util.ECRecipeManagerWrapper;
import com.expensivekoala.refined_avaritia.util.ExtendedCraftingUtil;
import com.expensivekoala.refined_avaritia.util.ExtendedCraftingUtil.TableSize;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiExtremePatternEncoder extends GuiContainer {
    private static final Map<String, ResourceLocation> TEXTURE_CACHE = new HashMap<>();

    private static final String GUI_TEXTURE = "gui/extreme_pattern_encoder.png";
    private static final String GUI_BUTTON = "gui/button.png";

    int screenWidth, screenHeight, lastButtonId;
    TableSize selectedTable = TableSize.ULTIMATE;
    TileExtremePatternEncoder tile;
    GuiCheckBox oredictPattern;
    public GuiCheckBox avaritia;

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
        if (Loader.isModLoaded("avaritia") && Loader.isModLoaded("extendedcrafting")) {
            avaritia = addCheckBox(guiLeft + 175, guiTop + 144, I18n.format("misc.refined_avaritia:avaritia"), tile.isAvaritia());
            if (avaritia.isChecked()) {
                RefinedAvaritia.instance.network.sendToServer(new MessageSetTableSize(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), TableSize.ULTIMATE));
                selectedTable = TableSize.ULTIMATE;
                tile.setTableSize(selectedTable);
            }
        }
        this.selectedTable = tile.getTableSize();
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
        return inBounds(200, 42, 16, 16, mouseX, mouseY) && tile.canCreatePattern();
    }

    private boolean isOverClear(int mouseX, int mouseY) {
        return inBounds(176, 8, 7, 7, mouseX, mouseY);
    }

    private boolean isOverBasic(int mouseX, int mouseY) {
        return inBounds(-14, 173, 50, 32, mouseX, mouseY);
    }

    private boolean isOverAdvanced(int mouseX, int mouseY) {
        return inBounds(202, 173, 50, 32, mouseX, mouseY);
    }

    private boolean isOverElite(int mouseX, int mouseY) {
        return inBounds(-14, 210, 50, 32, mouseX, mouseY);
    }

    private boolean isOverUltimate(int mouseX, int mouseY) {
        return inBounds(202, 210, 50, 32, mouseX, mouseY);
    }

    public boolean inBounds(int x, int y, int w, int h, int ox, int oy) {
        return ox >= x && ox <= x + w && oy >= y && oy <= y + h;
    }

    private boolean slotEnabled(int x, int y) {
        switch (selectedTable) {
            case BASIC:
                return x > 2 && x <= 5 && y > 2 && y <= 5;
            case ADVANCED:
                return x > 1 && x <= 6 && y > 1 && y <= 6;
            case ELITE:
                return x > 0 && x <= 7 && y > 0 && y <= 7;
            case ULTIMATE:
                return true;
        }
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button == oredictPattern) {
            tile.setOredictPattern(oredictPattern.isChecked());
            RefinedAvaritia.instance.network.sendToServer(new MessageSetOredictExtremePattern(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), oredictPattern.isChecked()));
        }

        if (Loader.isModLoaded("avaritia") && Loader.isModLoaded("extendedcrafting") && button == avaritia) {
            tile.setAvaritia(avaritia.isChecked());
            RefinedAvaritia.instance.network.sendToServer(new MessageSetAvaritiaPattern(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), avaritia.isChecked()));
            if (avaritia.isChecked()) {
                RefinedAvaritia.instance.network.sendToServer(new MessageSetTableSize(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), TableSize.ULTIMATE));
                selectedTable = TableSize.ULTIMATE;
                tile.setTableSize(selectedTable);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isOverClear(mouseX - guiLeft, mouseY - guiTop)) {
            RefinedAvaritia.instance.network.sendToServer(new MessageClearExtremePattern(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ()));

            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        } else if (isOverCreatePattern(mouseX - guiLeft, mouseY - guiTop)) {
            RefinedAvaritia.instance.network.sendToServer(new MessageCreateExtremePattern(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ()));

            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        } else if (Loader.isModLoaded("extendedcrafting")) {
            if (avaritia != null && avaritia.isChecked()) {
                return;
            }
            if (isOverBasic(mouseX - guiLeft, mouseY - guiTop)) {
                RefinedAvaritia.instance.network.sendToServer(new MessageSetTableSize(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), TableSize.BASIC));
                selectedTable = TableSize.BASIC;
                tile.setTableSize(selectedTable);

                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            } else if (isOverAdvanced(mouseX - guiLeft, mouseY - guiTop)) {
                RefinedAvaritia.instance.network.sendToServer(new MessageSetTableSize(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), TableSize.ADVANCED));
                selectedTable = TableSize.ADVANCED;
                tile.setTableSize(selectedTable);

                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            } else if (isOverElite(mouseX - guiLeft, mouseY - guiTop)) {
                RefinedAvaritia.instance.network.sendToServer(new MessageSetTableSize(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), TableSize.ELITE));
                selectedTable = TableSize.ELITE;
                tile.setTableSize(selectedTable);

                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            } else if (isOverUltimate(mouseX - guiLeft, mouseY - guiTop)) {
                RefinedAvaritia.instance.network.sendToServer(new MessageSetTableSize(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), TableSize.ULTIMATE));
                selectedTable = TableSize.ULTIMATE;
                tile.setTableSize(selectedTable);

                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        bindTexture(RefinedAvaritia.MODID, GUI_TEXTURE);

        drawTexture(guiLeft, guiTop, 0, 0, screenWidth, screenHeight);

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (!slotEnabled(x, y)) {
                    drawTexture(guiLeft + 11 + (x * 18), guiTop + 7 + (y * 18), 238, 49, 18, 18);
                }
            }
        }


        int ty = 0;

        if (isOverCreatePattern(mouseX - guiLeft, mouseY - guiTop))
            ty = 1;

        if (tile != null && !tile.canCreatePattern())
            ty = 2;

        drawTexture(guiLeft + 199, guiTop + 42, 240, ty * 16, 16, 16);

        if (Loader.isModLoaded("extendedcrafting")) {
            if(avaritia != null && avaritia.isChecked()) {
                return;
            }
            //Basic
            bindTexture(RefinedAvaritia.MODID, GUI_BUTTON);
            drawTexture(guiLeft - 14, guiTop + 173, selectedTable == TableSize.BASIC ? 0 : 50, 0, 50, 32);
            fontRenderer.drawString("3x3", guiLeft - 14 + 16, guiTop + 173 + 12, fontRenderer.getColorCode('0'));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //Advanced
            bindTexture(RefinedAvaritia.MODID, GUI_BUTTON);
            drawTexture(guiLeft + 202, guiTop + 173, selectedTable == TableSize.ADVANCED ? 0 : 50, 0, 50, 32);
            fontRenderer.drawString("5x5", guiLeft + 202 + 16, guiTop + 173 + 12, fontRenderer.getColorCode('0'));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //Elite
            bindTexture(RefinedAvaritia.MODID, GUI_BUTTON);
            drawTexture(guiLeft - 14, guiTop + 210, selectedTable == TableSize.ELITE ? 0 : 50, 0, 50, 32);
            fontRenderer.drawString("7x7", guiLeft - 14 + 16, guiTop + 210 + 12, fontRenderer.getColorCode('0'));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //Ultimate
            bindTexture(RefinedAvaritia.MODID, GUI_BUTTON);
            drawTexture(guiLeft + 202, guiTop + 210, selectedTable == TableSize.ULTIMATE ? 0 : 50, 0, 50, 32);
            fontRenderer.drawString("9x9", guiLeft + 202 + 16, guiTop + 210 + 12, fontRenderer.getColorCode('0'));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        mouseX -= guiLeft;
        mouseY -= guiTop;

        if (isOverClear(mouseX, mouseY)) {
            drawTooltip(mouseX, mouseY, I18n.format("misc.refined_avaritia:clear_tooltip"));
        }

        if (isOverCreatePattern(mouseX, mouseY)) {
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
