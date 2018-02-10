package com.expensivekoala.refined_avaritia.util;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.crafting.table.basic.BasicStackHandler;
import com.blakebr0.extendedcrafting.tile.TileAdvancedCraftingTable;
import com.blakebr0.extendedcrafting.tile.TileBasicCraftingTable;
import com.blakebr0.extendedcrafting.tile.TileEliteCraftingTable;
import com.blakebr0.extendedcrafting.tile.TileUltimateCraftingTable;
import com.expensivekoala.refined_avaritia.Registry;
import com.expensivekoala.refined_avaritia.item.ItemExtremePattern;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;

public class PatternEventHandler {

    @SubscribeEvent
    public void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        if(!event.getWorld().isRemote) {
            if(Loader.isModLoaded("extendedcrafting")) {
                if (event.getEntityPlayer().getHeldItem(event.getHand()).getItem() == Registry.PATTERN
                        && !event.getEntityPlayer().getHeldItem(event.getHand()).hasTagCompound()) {
                    if (event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.blockBasicTable) {
                        if(!((TileBasicCraftingTable)event.getWorld().getTileEntity(event.getPos())).getResult().isEmpty()) {
                            setRecipe(event.getEntityPlayer().getHeldItem(event.getHand()),
                                    ((TileBasicCraftingTable)event.getWorld().getTileEntity(event.getPos())).getMatrix(),
                                    RecipeType.EC_BASIC);
                        }
                    } else if(event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.blockAdvancedTable) {
                        if(!((TileAdvancedCraftingTable)event.getWorld().getTileEntity(event.getPos())).getResult().isEmpty()) {
                            setRecipe(event.getEntityPlayer().getHeldItem(event.getHand()),
                                    (ItemStackHandler)((TileAdvancedCraftingTable)event.getWorld().getTileEntity(event.getPos())).getMatrix(),
                                    RecipeType.EC_ADVANCED);
                        }
                    } else if(event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.blockEliteTable) {
                        if(!((TileEliteCraftingTable)event.getWorld().getTileEntity(event.getPos())).getResult().isEmpty()) {
                            setRecipe(event.getEntityPlayer().getHeldItem(event.getHand()),
                                    ((TileEliteCraftingTable)event.getWorld().getTileEntity(event.getPos())).getMatrix(),
                                    RecipeType.EC_ELITE);
                        }
                    } else if(event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.blockUltimateTable) {
                        if(!((TileUltimateCraftingTable)event.getWorld().getTileEntity(event.getPos())).getResult().isEmpty()) {
                            setRecipe(event.getEntityPlayer().getHeldItem(event.getHand()),
                                    ((TileUltimateCraftingTable)event.getWorld().getTileEntity(event.getPos())).getMatrix(),
                                    RecipeType.EC_ULTIMATE);
                        }
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

    private void setRecipe(ItemStack pattern, ItemStackHandler recipe, RecipeType type) {

        ItemExtremePattern.setOredict(pattern, false);

        ItemExtremePattern.setType(pattern, type);

        for (int i = 0; i < type.height * type.width; i++) {
            ItemStack ingredient = recipe.getStackInSlot(i).copy();
            ingredient.setCount(1);
            if(!ingredient.isEmpty()) {
                ItemExtremePattern.setSlot(pattern, i, ingredient);
            }
        }
    }
}
