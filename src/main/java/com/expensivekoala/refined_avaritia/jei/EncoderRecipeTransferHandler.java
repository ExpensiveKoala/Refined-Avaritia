package com.expensivekoala.refined_avaritia.jei;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.gui.ContainerExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.network.MessageTransferAvaritiaRecipe;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
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
public class EncoderRecipeTransferHandler implements IRecipeTransferHandler<ContainerExtremePatternEncoder> {
    @Override
    public Class<ContainerExtremePatternEncoder> getContainerClass() {
        return ContainerExtremePatternEncoder.class;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(ContainerExtremePatternEncoder container, IRecipeLayout recipeLayout, EntityPlayer player, boolean maxTransfer, boolean doTransfer) {
        if(doTransfer) {
            Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngredients = recipeLayout.getItemStacks().getGuiIngredients();

            InventoryCrafting inventory = new InventoryCrafting(container, 9, 9);
            NonNullList<ItemStack> items = NonNullList.withSize(81, ItemStack.EMPTY);

            for (Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : guiIngredients.entrySet()) {
                int recipeSlot = entry.getKey();
                List<ItemStack> allIngredients = entry.getValue().getAllIngredients();
                if (!allIngredients.isEmpty()) {
                    if (recipeSlot != 0) { // skip the output slot
                        ItemStack firstIngredient = allIngredients.get(0);
                        inventory.setInventorySlotContents(recipeSlot-1, firstIngredient);
                        items.set(recipeSlot-1, firstIngredient);
                    }
                }
            }

            IExtremeRecipe recipe = null;
            for(IExtremeRecipe extremeRecipe :  AvaritiaRecipeManager.EXTREME_RECIPES.values()) {
                if(extremeRecipe.matches(inventory, player.getEntityWorld())) {
                    recipe = extremeRecipe;
                    break;
                }
            }

            if(recipe != null) {
                RefinedAvaritia.instance.network.sendToServer(
                        new MessageTransferAvaritiaRecipe(
                                container.getTile().getPos().getX(),
                                container.getTile().getPos().getY(),
                                container.getTile().getPos().getZ(),
                                items));
            }
        }

        return null;
    }
}
