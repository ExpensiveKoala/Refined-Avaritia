package com.expensivekoala.refined_avaritia.util;

import morph.avaritia.recipe.AvaritiaRecipeManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class AvaritiaRecipeManagerWrapper {
    public static ItemStack getCraftingResult(InventoryCrafting crafting, World world) {
        ItemStack result = ItemStack.EMPTY;
        if(Loader.isModLoaded("avaritia")) {
            result = AvaritiaRecipeManager.getExtremeCraftingResult(crafting, world);
        }
        return result;
    }
}
