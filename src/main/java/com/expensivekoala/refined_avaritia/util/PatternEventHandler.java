package com.expensivekoala.refined_avaritia.util;

import com.expensivekoala.refined_avaritia.item.ItemExtremePattern;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;

public class PatternEventHandler {

    @SubscribeEvent
    public void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        if(!event.getWorld().isRemote) {
            if(Loader.isModLoaded("extendedcrafting")) {
                ExtendedCraftingUtil.handlePatternRightClick(event);
            }
        }
    }

    public static void setRecipe(ItemStack pattern, NonNullList<ItemStack> recipe, RecipeType type) {

        ItemExtremePattern.setOredict(pattern, false);

        ItemExtremePattern.setType(pattern, type);

        for (int i = 0; i < type.height * type.width; i++) {
            ItemStack ingredient = recipe.get(i).copy();
            ingredient.setCount(1);
            if(!ingredient.isEmpty()) {
                ItemExtremePattern.setSlot(pattern, i, ingredient);
            }
        }
    }
}
