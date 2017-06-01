package com.expensivekoala.refined_avaritia.util;

import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.apiimpl.util.Comparer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ExtremePattern implements ICraftingPattern {

    private ICraftingPatternContainer container;
    private ItemStack stack;
    private IRecipe recipe;
    private List<ItemStack> inputs = new ArrayList<>();
    private List<List<ItemStack>> oreInputs = new ArrayList<>();
    private List<ItemStack> outputs = new ArrayList<>();
    private List<ItemStack> byproducts = new ArrayList<>();

    public ExtremePattern(World world, ICraftingPatternContainer container, ItemStack stack) {
        this.container = container;
        this.stack = Comparer.stripTags(stack);

        InventoryCrafting inv = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer playerIn)
                {
                    return false;
                }
        }, 9, 9);

        for (int i = 0; i < 81; i++) {

        }
    }

    @Override
    public ICraftingPatternContainer getContainer()
        {
            return null;
        }

    @Override
    public ItemStack getStack()
        {
            return null;
        }

    @Override
    public boolean isValid()
        {
            return false;
        }

    @Override
    public boolean isProcessing()
        {
            return false;
        }

    @Override
    public boolean isOredict()
        {
            return false;
        }

    @Override
    public List<ItemStack> getInputs()
        {
            return null;
        }

    @Override
    public List<List<ItemStack>> getOreInputs()
        {
            return null;
        }

    @Nullable
    @Override
    public List<ItemStack> getOutputs(ItemStack[] itemStacks)
        {
            return null;
        }

    @Override
    public List<ItemStack> getOutputs()
        {
            return null;
        }

    @Override
    public List<ItemStack> getByproducts(ItemStack[] itemStacks)
        {
            return null;
        }

    @Override
    public List<ItemStack> getByproducts()
        {
            return null;
        }

    @Override
    public String getId()
        {
            return null;
        }

    @Override
    public int getQuantityPerRequest(ItemStack itemStack, int i)
        {
            return 0;
        }

    @Override
    public ItemStack getActualOutput(ItemStack itemStack, int i)
        {
            return null;
        }
}
