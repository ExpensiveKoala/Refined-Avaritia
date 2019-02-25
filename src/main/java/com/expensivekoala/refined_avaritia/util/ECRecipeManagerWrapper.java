package com.expensivekoala.refined_avaritia.util;

import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.AdvancedTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.BasicTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.EliteTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.UltimateTableCategory;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class ECRecipeManagerWrapper {
    public static final String[] EXTREME_CRAFTING = new String[] {BasicTableCategory.UID, AdvancedTableCategory.UID, EliteTableCategory.UID, UltimateTableCategory.UID};
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
