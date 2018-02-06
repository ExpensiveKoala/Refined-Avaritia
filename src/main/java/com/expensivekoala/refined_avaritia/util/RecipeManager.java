package com.expensivekoala.refined_avaritia.util;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class RecipeManager {
    public static ItemStack getCraftingResult(InventoryCrafting crafting, World world) {
        ItemStack result = ItemStack.EMPTY;
        if(Loader.isModLoaded("avaritia")) {
            result = AvaritiaRecipeManager.getExtremeCraftingResult(crafting, world);
        }
        if(Loader.isModLoaded("extendedcrafting")) {
            ItemStack extendedResult = TableRecipeManager.getInstance().findMatchingRecipe(crafting, world);
            if(!extendedResult.isEmpty()) {
                result = extendedResult;
            }
        }
        return result;
    }
}
