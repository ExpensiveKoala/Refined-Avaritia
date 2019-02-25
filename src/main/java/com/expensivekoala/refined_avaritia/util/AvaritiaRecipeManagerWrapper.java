package com.expensivekoala.refined_avaritia.util;

import morph.avaritia.compat.jei.AvaritiaJEIPlugin;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import java.util.Collection;

public class AvaritiaRecipeManagerWrapper {
    public static final String EXTREME_CRAFTING = AvaritiaJEIPlugin.EXTREME_CRAFTING;
    public static ItemStack getCraftingResult(InventoryCrafting crafting, World world) {
        ItemStack result = ItemStack.EMPTY;
        if(Loader.isModLoaded("avaritia")) {
            result = AvaritiaRecipeManager.getExtremeCraftingResult(crafting, world);
        }
        return result;
    }
    public static Collection<IExtremeRecipe> getRecipes() {
        return AvaritiaRecipeManager.EXTREME_RECIPES.values();
    }
}
