package com.expensivekoala.refined_avaritia.util;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.item.ItemExtremePattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.apiimpl.autocrafting.registry.CraftingTaskFactory;
import com.raoulvdberge.refinedstorage.item.ItemPattern;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ExtremePattern implements ICraftingPattern {

    private ICraftingPatternContainer container;
    private ItemStack stack;
    private IExtremeRecipe avaritiaRecipe;
    private IRecipe extendedRecipe;
    private RecipeType recipeType;
    private boolean processing;
    private boolean oredict;
    private boolean valid;
    private List<NonNullList<ItemStack>> inputs = new ArrayList<>();
    private NonNullList<ItemStack> outputs = NonNullList.create();
    private NonNullList<ItemStack> byproducts = NonNullList.create();

    public ExtremePattern(World world, ICraftingPatternContainer container, ItemStack stack) {
        this.container = container;
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(ItemExtremePattern.NBT_TYPE)) {
            this.recipeType = ItemExtremePattern.getType(stack);
        } else {
            RefinedAvaritia.logger.warn("Assuming Avaritia recipe. If you see this message, remake your pattern. " + stack);
            this.recipeType = RecipeType.AVARITIA;
        }
        this.stack = stack;
        this.processing = false;
        this.oredict = ItemExtremePattern.isOredict(stack);

        InventoryCrafting inv = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, recipeType.width, recipeType.height);

        for (int i = 0; i < (recipeType.width * recipeType.height); ++i) {
            ItemStack slot = ItemExtremePattern.getSlot(stack, i);

            inputs.add(slot == null ? NonNullList.create() : NonNullList.from(ItemStack.EMPTY, slot));

            if (slot != null) {
                inv.setInventorySlotContents(i, slot);
            }
        }

        if (!ItemPattern.isProcessing(stack)) {
            if (recipeType == RecipeType.AVARITIA) {
                for (IExtremeRecipe r : AvaritiaRecipeManager.EXTREME_RECIPES.values()) {
                    if (r.matches(inv, world)) {
                        avaritiaRecipe = r;
                        break;
                    }
                }
            } else {
                for (Object o : TableRecipeManager.getInstance().getRecipes()) {
                    IRecipe r = (IRecipe) o;
                    if (r.matches(inv, world)) {
                        extendedRecipe = r;
                        break;
                    }
                }
            }

            if (avaritiaRecipe != null || extendedRecipe != null) {
                ItemStack output = recipeType == RecipeType.AVARITIA ? avaritiaRecipe.getCraftingResult(inv) : extendedRecipe.getCraftingResult(inv);

                if (!output.isEmpty()) {
                    valid = true;
                    outputs.add(output.copy());
                    byproducts = recipeType == RecipeType.AVARITIA ? avaritiaRecipe.getRemainingItems(inv) : extendedRecipe.getRemainingItems(inv);

                    if (oredict) {
                        int size = 0;
                        if (recipeType == RecipeType.AVARITIA && avaritiaRecipe != null) {
                            size = avaritiaRecipe.getIngredients().size();
                        } else if (extendedRecipe != null) {
                            size = extendedRecipe.getIngredients().size();
                        } else {
                            valid = false;
                        }
                        if (size > 0) {
                            inputs.clear();
                            for (Ingredient ingredient : recipeType == RecipeType.AVARITIA ? avaritiaRecipe.getIngredients() : extendedRecipe.getIngredients()) {
                                inputs.add(NonNullList.from(ItemStack.EMPTY, ingredient.getMatchingStacks()));
                            }
                        } else {
                            valid = false;
                        }
                    }
                }
            }
        }
    }

    @Override
    public ICraftingPatternContainer getContainer() {
        return container;
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean isProcessing() {
        return false;
    }

    @Override
    public boolean isOredict() {
        return oredict;
    }


    @Override
    public NonNullList<ItemStack> getByproducts(NonNullList<ItemStack> took) {
        if (took.size() != inputs.size()) {
            throw new IllegalArgumentException("The items that are taken (" + took.size() + ") should match the inputs for this pattern (" + inputs.size() + ")");
        }

        InventoryCrafting inv = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, recipeType.width, recipeType.height);

        for (int i = 0; i < took.size(); ++i) {
            inv.setInventorySlotContents(i, took.get(i));
        }

        NonNullList<ItemStack> remainingItems = recipeType == RecipeType.AVARITIA ? avaritiaRecipe.getRemainingItems(inv) : extendedRecipe.getRemainingItems(inv);
        NonNullList<ItemStack> sanitized = NonNullList.create();

        for (ItemStack item : remainingItems) {
            if (!item.isEmpty()) {
                sanitized.add(item);
            }
        }

        return sanitized;
    }

    @Override
    public String getId() {
        return CraftingTaskFactory.ID;
    }

    @Override
    public List<NonNullList<ItemStack>> getInputs() {
        return inputs;
    }

    @Override
    public NonNullList<ItemStack> getOutputs() {
        return outputs;
    }

    @Override
    public ItemStack getOutput(NonNullList<ItemStack> took) {


        if (took.size() != inputs.size()) {
            throw new IllegalArgumentException("The items that are taken (" + took.size() + ") should match the inputs for this pattern (" + inputs.size() + ")");
        }

        InventoryCrafting inv = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, recipeType.width, recipeType.height);

        for (int i = 0; i < took.size(); ++i) {
            inv.setInventorySlotContents(i, took.get(i));
        }

        ItemStack result = recipeType == RecipeType.AVARITIA ? avaritiaRecipe.getCraftingResult(inv) : extendedRecipe.getCraftingResult(inv);
        if (result.isEmpty()) {
            throw new IllegalStateException("Cannot have empty result");
        }

        return result;
    }

    @Override
    public NonNullList<ItemStack> getByproducts() {
        return byproducts;
    }

    @Override
    public NonNullList<FluidStack> getFluidInputs() {
        return NonNullList.create();
    }

    @Override
    public NonNullList<FluidStack> getFluidOutputs() {
        return NonNullList.create();
    }

    @Override
    public boolean canBeInChainWith(ICraftingPattern other) {
        if (other.isProcessing() || other.isOredict() != oredict) {
            return false;
        }

        if ((other.getInputs().size() != inputs.size()) ||
          (other.getFluidInputs().size() > 0) ||
          (other.getOutputs().size() != outputs.size()) ||
          (other.getFluidOutputs().size() > 0)) {
            return false;
        }

        if (other.getByproducts().size() != byproducts.size()) {
            return false;
        }

        for (int i = 0; i < inputs.size(); ++i) {
            List<ItemStack> inputs = this.inputs.get(i);
            List<ItemStack> otherInputs = other.getInputs().get(i);

            if (inputs.size() != otherInputs.size()) {
                return false;
            }

            for (int j = 0; j < inputs.size(); ++j) {
                if (!API.instance().getComparer().isEqual(inputs.get(j), otherInputs.get(j))) {
                    return false;
                }
            }
        }

        for (int i = 0; i < outputs.size(); ++i) {
            if (!API.instance().getComparer().isEqual(outputs.get(i), other.getOutputs().get(i))) {
                return false;
            }
        }


        for (int i = 0; i < byproducts.size(); ++i) {
            if (!API.instance().getComparer().isEqual(byproducts.get(i), other.getByproducts().get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getChainHashCode() {
        int result = 0;

        result = 31 * result + (oredict ? 1 : 0);

        for (List<ItemStack> inputs : this.inputs) {
            for (ItemStack input : inputs) {
                result = 31 * result + API.instance().getItemStackHashCode(input);
            }
        }

        for (ItemStack output : this.outputs) {
            result = 31 * result + API.instance().getItemStackHashCode(output);
        }

        for (ItemStack byproduct : this.byproducts) {
            result = 31 * result + API.instance().getItemStackHashCode(byproduct);
        }

        return result;
    }
}
