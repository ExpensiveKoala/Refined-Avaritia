package com.expensivekoala.refined_avaritia.util;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class ECRecipeManagerWrapper {
    public static ItemStack getCraftingResult(InventoryCrafting crafting, World world) {
        ItemStack result = ItemStack.EMPTY;
        if(Loader.isModLoaded("extendedcrafting")) {
            ItemStack extendedResult = TableRecipeManager.getInstance().findMatchingRecipe(crafting, world);
            if(!extendedResult.isEmpty()) {
                result = extendedResult;
            }
        }
        return result;
    }
}
