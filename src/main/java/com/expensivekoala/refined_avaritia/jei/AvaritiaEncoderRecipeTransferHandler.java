package com.expensivekoala.refined_avaritia.jei;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.gui.ContainerExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.gui.GuiExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.network.MessageTransferAvaritiaRecipe;
import com.expensivekoala.refined_avaritia.util.ExtendedCraftingUtil;
import com.expensivekoala.refined_avaritia.util.RecipeType;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author ExpensiveKoala
 */
public class AvaritiaEncoderRecipeTransferHandler implements IRecipeTransferHandler<ContainerExtremePatternEncoder> {
    @Override
    public Class<ContainerExtremePatternEncoder> getContainerClass() {
        return ContainerExtremePatternEncoder.class;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(ContainerExtremePatternEncoder container, IRecipeLayout recipeLayout, EntityPlayer player, boolean maxTransfer, boolean doTransfer) {
        if (doTransfer) {
            Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngredients = recipeLayout.getItemStacks().getGuiIngredients();

            InventoryCrafting inventory = new InventoryCrafting(container, 9, 9);
            NonNullList<ItemStack> items = NonNullList.withSize(81, ItemStack.EMPTY);

            for (Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : guiIngredients.entrySet()) {
                int recipeSlot = entry.getKey();
                List<ItemStack> allIngredients = entry.getValue().getAllIngredients();
                if (!allIngredients.isEmpty()) {
                    if (recipeSlot != 0) { // skip the output slot
                        ItemStack firstIngredient = allIngredients.get(0);
                        inventory.setInventorySlotContents(recipeSlot - 1, firstIngredient);
                        items.set(recipeSlot - 1, firstIngredient);
                    }
                }
            }

            container.getTile().setTableSize(ExtendedCraftingUtil.TableSize.ULTIMATE);
            container.getTile().setAvaritia(true);
            if(player.world.isRemote) {
                Gui gui = Minecraft.getMinecraft().currentScreen;
                if(gui instanceof GuiExtremePatternEncoder && ((GuiExtremePatternEncoder) gui).avaritia != null) {
                    ((GuiExtremePatternEncoder) gui).avaritia.setIsChecked(true);
                }
            }

            RefinedAvaritia.instance.network.sendToServer(
              new MessageTransferAvaritiaRecipe(
                container.getTile().getPos().getX(),
                container.getTile().getPos().getY(),
                container.getTile().getPos().getZ(),
                items, RecipeType.AVARITIA));
        }

        return null;
    }
}
