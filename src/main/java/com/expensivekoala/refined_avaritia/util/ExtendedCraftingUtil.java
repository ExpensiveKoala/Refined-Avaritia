package com.expensivekoala.refined_avaritia.util;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.tile.TileAdvancedCraftingTable;
import com.blakebr0.extendedcrafting.tile.TileBasicCraftingTable;
import com.blakebr0.extendedcrafting.tile.TileEliteCraftingTable;
import com.blakebr0.extendedcrafting.tile.TileUltimateCraftingTable;
import com.expensivekoala.refined_avaritia.Registry;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * @author ExpensiveKoala
 */
public class ExtendedCraftingUtil {

    public enum TableSize {
        BASIC,
        ADVANCED,
        ELITE,
        ULTIMATE
    }

    public static void handlePatternRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntityPlayer().getHeldItem(event.getHand()).getItem() == Registry.PATTERN
          && !event.getEntityPlayer().getHeldItem(event.getHand()).hasTagCompound()) {
            if (event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.blockBasicTable) {
                if(!((TileBasicCraftingTable)event.getWorld().getTileEntity(event.getPos())).getResult().isEmpty()) {
                    PatternEventHandler.setRecipe(event.getEntityPlayer().getHeldItem(event.getHand()),
                      ((TileBasicCraftingTable)event.getWorld().getTileEntity(event.getPos())).getMatrix(),
                      RecipeType.EC_BASIC);
                    event.setCanceled(true);
                }
            } else if(event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.blockAdvancedTable) {
                if(!((TileAdvancedCraftingTable)event.getWorld().getTileEntity(event.getPos())).getResult().isEmpty()) {
                    PatternEventHandler.setRecipe(event.getEntityPlayer().getHeldItem(event.getHand()),
                      ((TileAdvancedCraftingTable)event.getWorld().getTileEntity(event.getPos())).getMatrix(),
                      RecipeType.EC_ADVANCED);
                    event.setCanceled(true);
                }
            } else if(event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.blockEliteTable) {
                if(!((TileEliteCraftingTable)event.getWorld().getTileEntity(event.getPos())).getResult().isEmpty()) {
                    PatternEventHandler.setRecipe(event.getEntityPlayer().getHeldItem(event.getHand()),
                      ((TileEliteCraftingTable)event.getWorld().getTileEntity(event.getPos())).getMatrix(),
                      RecipeType.EC_ELITE);
                    event.setCanceled(true);
                }
            } else if(event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.blockUltimateTable) {
                if(!((TileUltimateCraftingTable)event.getWorld().getTileEntity(event.getPos())).getResult().isEmpty()) {
                    PatternEventHandler.setRecipe(event.getEntityPlayer().getHeldItem(event.getHand()),
                      ((TileUltimateCraftingTable)event.getWorld().getTileEntity(event.getPos())).getMatrix(),
                      RecipeType.EC_ULTIMATE);
                    event.setCanceled(true);
                }
            }
        }
    }
}
