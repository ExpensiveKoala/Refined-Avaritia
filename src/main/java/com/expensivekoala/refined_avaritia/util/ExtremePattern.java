package com.expensivekoala.refined_avaritia.util;

import com.expensivekoala.refined_avaritia.item.ItemExtremePattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.apiimpl.autocrafting.registry.CraftingTaskFactory;
import com.raoulvdberge.refinedstorage.apiimpl.util.Comparer;
import com.raoulvdberge.refinedstorage.item.ItemPattern;
import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import morph.avaritia.recipe.extreme.ExtremeShapedOreRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

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
            ItemStack slot = ItemExtremePattern.getSlot(stack, i);
            inputs.add(slot);
            inv.setInventorySlotContents(i, slot);
        }

        recipe = (IRecipe)ExtremeCraftingManager.getInstance().getRecipeList().stream().filter(r -> ((IRecipe)r).matches(inv, world)).findFirst().orElse(null);
        if(recipe != null) {
            ItemStack output = recipe.getCraftingResult(inv);
            if(!output.isEmpty()) {
                boolean shapedOre = recipe instanceof ExtremeShapedOreRecipe;
                outputs.add(Comparer.stripTags(output.copy()));

                if(isOredict() && shapedOre) {
                    Object[] inputs = new Object[0];
                    if(shapedOre) {
                        inputs = ((ExtremeShapedOreRecipe)recipe).getInput();
                    } else {
                        try {
                            inputs = (Object[])recipe.getClass().getMethod("getInput").invoke(recipe);
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                    for (Object input : inputs) {
                        if(input == null) {
                            oreInputs.add(Collections.emptyList());
                        } else if (input instanceof ItemStack) {
                            oreInputs.add(Collections.singletonList(Comparer.stripTags((ItemStack)input).copy()));
                        } else {
                            List<ItemStack> cleaned = new LinkedList<>();
                            for (ItemStack in : (List<ItemStack>)input) {
                                cleaned.add(Comparer.stripTags(in.copy()));
                            }

                            oreInputs.add(cleaned);
                        }
                    }
                }

                for(ItemStack remaining : recipe.getRemainingItems(inv)) {
                    if (!remaining.isEmpty()) {
                        byproducts.add(Comparer.stripTags(remaining.copy()));
                    }
                }
            }
        }

        if(oreInputs.isEmpty()) {
            for(ItemStack input : inputs) {
                if(input.isEmpty()) {
                    oreInputs.add(Collections.emptyList());
                } else if(isOredict()) {
                    int[] ids = OreDictionary.getOreIDs(input);
                    if(ids == null || ids.length == 0) {
                        oreInputs.add(Collections.singletonList(Comparer.stripTags(input)));
                    } else {
                        List<ItemStack> oredict =
                                Arrays.stream(ids)
                                        .mapToObj(OreDictionary::getOreName)
                                        .map(OreDictionary::getOres)
                                        .flatMap(List::stream)
                                        .map(ItemStack::copy)
                                        .map(Comparer::stripTags)
                                        .map(s -> {
                                            s.setCount(input.getCount());
                                            return s;
                                        })
                                        .collect(Collectors.toList());
                        // Add original stack as first, should prevent some issues
                        oredict.add(0, Comparer.stripTags(input.copy()));
                        oreInputs.add(oredict);
                    }
                } else {
                    oreInputs.add(Collections.singletonList(Comparer.stripTags(input)));
                }
            }
        }
    }

    @Override
    public ICraftingPatternContainer getContainer()
        {
            return container;
        }

    @Override
    public ItemStack getStack()
        {
            return stack;
        }

    @Override
    public boolean isValid() {
        return inputs.stream().filter(Objects::nonNull).count() > 0 && !outputs.isEmpty();
    }

    @Override
    public boolean isProcessing()
        {
            return false;
        }

    @Override
    public boolean isOredict()
        {
            return ItemExtremePattern.isOredict(stack);
        }

    @Override
    public boolean isBlocking() {
        return ItemPattern.isBlocking(stack);
    }

    @Override
    public List<ItemStack> getInputs()
        {
            return inputs;
        }

    @Override
    public List<List<ItemStack>> getOreInputs()
        {
            return oreInputs;
        }

    @Nullable
    @Override
    public List<ItemStack> getOutputs(ItemStack[] itemStacks)
    {
        List<ItemStack> outputs = new ArrayList<>();

        InventoryCrafting inv = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer playerIn) {
                return false;
            }
        }, 9, 9);

        for (int i = 0; i < 81; i++) {
            inv.setInventorySlotContents(i, itemStacks[i]);
        }

        ItemStack cleaned = recipe.getCraftingResult(inv);
        if(cleaned.isEmpty()) {
            return null;
        }
        outputs.add(cleaned.copy());

        return outputs;

    }

    @Override
    public List<ItemStack> getOutputs()
        {
            return outputs;
        }

    @Override
    public List<ItemStack> getByproducts(ItemStack[] itemStacks)
    {
        List<ItemStack> byproducts = new ArrayList<>();

        InventoryCrafting inv = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, 9, 9);

        for (int i = 0; i < 81; ++i) {
            inv.setInventorySlotContents(i, itemStacks[i]);
        }

        for (ItemStack remaining : recipe.getRemainingItems(inv)) {
            if (!remaining.isEmpty()) {
                byproducts.add(remaining.copy());
            }
        }
        return byproducts;
    }

    @Override
    public List<ItemStack> getByproducts()
        {
            return byproducts;
        }

    @Override
    public String getId()
        {
            return CraftingTaskFactory.ID;
        }

    @Override
    public int getQuantityPerRequest(ItemStack itemStack, int i)
    {
        int quantity = 0;
        itemStack = Comparer.stripTags(itemStack.copy());
        for(ItemStack output : outputs) {
            if(API.instance().getComparer().isEqual(itemStack, output, i)) {
                quantity += output.getCount();
            }
        }
        return quantity;
    }

    @Override
    public ItemStack getActualOutput(ItemStack itemStack, int i)
    {
        itemStack = Comparer.stripTags(itemStack.copy());
        for(ItemStack output : outputs) {
            if(API.instance().getComparer().isEqual(itemStack, output, i)) {
                return output.copy();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean alike(ICraftingPattern other) {
        if (other == this) {
            return true;
        }

        if (other.getId().equals(this.getId())
                && other.isOredict() == this.isOredict()
                && other.isBlocking() == this.isBlocking()
                && other.isProcessing() == this.isProcessing()
                && other.getOreInputs().size() == this.getOreInputs().size()
                && other.getOutputs().size() == this.getOutputs().size()) {
            boolean same = true;
            for (int i = 0; i < other.getOreInputs().size(); i++) {
                same &= other.getOreInputs().get(i).size() == this.getOreInputs().get(i).size();
            }
            int j = 0;
            while (same && j < other.getOutputs().size()) {
                same = ItemStack.areItemStacksEqual(other.getOutputs().get(j), this.getOutputs().get(j));
                j++;
            }
            int i = 0;
            while (same && i < other.getOreInputs().size()) {
                List<ItemStack> otherList = other.getOreInputs().get(i);
                List<ItemStack> thisList = this.getOreInputs().get(i);
                j = 0;
                while (same && j < otherList.size()) {
                    same = ItemStack.areItemStacksEqual(otherList.get(j), thisList.get(j));
                    j++;
                }
                i++;
            }
            return same;
        }
        return false;
    }
}
