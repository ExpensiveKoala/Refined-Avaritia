package com.expensivekoala.refined_avaritia;

import com.expensivekoala.refined_avaritia.block.BlockExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.item.ItemExtremePattern;
import com.raoulvdberge.refinedstorage.RSBlocks;
import com.raoulvdberge.refinedstorage.RSItems;
import morph.avaritia.init.ModBlocks;
import morph.avaritia.init.ModItems;
import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * @author ExpensiveKoala
 */
public class Registry {

    //Items
    public static final ItemExtremePattern PATTERN = new ItemExtremePattern();

    //Blocks
    public static final BlockExtremePatternEncoder EXTREME_PATTERN_ENCODER = new BlockExtremePatternEncoder();


    public static void initRecipes() {
        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(PATTERN, 1),
                "GPG",
                "PGP",
                "NNN",
                'G', "blockGlass",
                'P', new ItemStack(RSItems.PATTERN),
                'N', ModItems.neutronium_ingot
        ));

        GameRegistry.addShapedRecipe(
                new ItemStack(EXTREME_PATTERN_ENCODER, 1),
                "NCN",
                "PBP",
                "NQN",
                'N', ModItems.neutronium_ingot,
                'C', ModBlocks.dire_craft,
                'P', new ItemStack(PATTERN),
                'Q', ModItems.infinity_ingot,
                'B', new ItemStack(RSBlocks.MACHINE_CASING)
        );
    }

}
