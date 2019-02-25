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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static net.minecraft.client.Minecraft.getMinecraft;

/**
 * @author ExpensiveKoala
 */
public class ECBasicEncoderRecipeTransferHandler implements IRecipeTransferHandler<ContainerExtremePatternEncoder> {
    @Override
    public Class<ContainerExtremePatternEncoder> getContainerClass() {
        return ContainerExtremePatternEncoder.class;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(ContainerExtremePatternEncoder container, IRecipeLayout recipeLayout, EntityPlayer player, boolean maxTransfer, boolean doTransfer) {
        if (doTransfer) {
            Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngredients = recipeLayout.getItemStacks().getGuiIngredients();

            InventoryCrafting inventory = new InventoryCrafting(container, 3, 3);
            NonNullList<ItemStack> items = NonNullList.withSize(3*3, ItemStack.EMPTY);

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

            container.getTile().setTableSize(ExtendedCraftingUtil.TableSize.BASIC);
            container.getTile().setAvaritia(false);
            if(player.world.isRemote) {
                Gui gui = Minecraft.getMinecraft().currentScreen;
                if(gui instanceof GuiExtremePatternEncoder) {
                    ((GuiExtremePatternEncoder) gui).avaritia.setIsChecked(false);
                }
            }

            RefinedAvaritia.instance.network.sendToServer(
              new MessageTransferAvaritiaRecipe(
                container.getTile().getPos().getX(),
                container.getTile().getPos().getY(),
                container.getTile().getPos().getZ(),
                items, RecipeType.EC_BASIC));
        }

        return null;
    }
}
