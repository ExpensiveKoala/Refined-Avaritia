package com.expensivekoala.refined_avaritia.item;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.Registry;
import com.expensivekoala.refined_avaritia.util.ExtremePattern;
import com.expensivekoala.refined_avaritia.util.RecipeType;
import com.raoulvdberge.refinedstorage.RS;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.util.StackUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ItemExtremePattern extends Item implements ICraftingPatternProvider{

    /**
     * Blatantly copying Refined Storage <3
     */
    private static Map<ItemStack, ExtremePattern> PATTERN_CACHE = new HashMap<>();

    private static final String NBT_SLOT = "Slot_%d";
    private static final String NBT_OREDICT = "Oredict";
    public static final String NBT_TYPE = "Type";

    public ItemExtremePattern() {
        setRegistryName(RefinedAvaritia.MODID, "extreme_pattern");
        setCreativeTab(RS.INSTANCE.tab);
    }

    @Override
    public String getUnlocalizedName() {
        return "item." + RefinedAvaritia.MODID + ":extreme_pattern";
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName();
    }

    public static ExtremePattern getPatternFromCache(World world, ItemStack stack) {
        if(!PATTERN_CACHE.containsKey(stack)) {
            PATTERN_CACHE.put(stack, new ExtremePattern(world, null, stack));
        }
        return PATTERN_CACHE.get(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(!stack.hasTagCompound()) {
            return;
        }

        ICraftingPattern pattern = getPatternFromCache(worldIn, stack);

        if (pattern.isValid()) {
            if (GuiScreen.isShiftKeyDown()) {
                tooltip.add(TextFormatting.YELLOW + I18n.format("misc.refinedstorage:pattern.inputs") + TextFormatting.RESET);

                combineItems(tooltip, true, StackUtils.toNonNullList(pattern.getInputs()));

                tooltip.add(TextFormatting.YELLOW + I18n.format("misc.refinedstorage:pattern.outputs") + TextFormatting.RESET);
            }

            combineItems(tooltip, true, StackUtils.toNonNullList(pattern.getOutputs()));

            if (isOredict(stack)) {
                tooltip.add(TextFormatting.BLUE + I18n.format("misc.refinedstorage:pattern.oredict") + TextFormatting.RESET);
            }
        } else {
            tooltip.add(TextFormatting.RED + I18n.format("misc.refinedstorage:pattern.invalid") + TextFormatting.RESET);
        }
    }

    public static void setSlot(ItemStack pattern, int slot, ItemStack stack) {
        // Safety against bad stacks
        if (stack.getCount() < 0) {
            stack.setCount(1);
        }

        if (!pattern.hasTagCompound()) {
            pattern.setTagCompound(new NBTTagCompound());
        }

        pattern.getTagCompound().setTag(String.format(NBT_SLOT, slot), stack.serializeNBT());
    }

    public static ItemStack getSlot(ItemStack pattern, int slot) {
        String id = String.format(NBT_SLOT, slot);

        if (!pattern.hasTagCompound() || !pattern.getTagCompound().hasKey(id)) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(pattern.getTagCompound().getCompoundTag(id));
    }

    public static boolean isOredict(ItemStack pattern) {
        return pattern.hasTagCompound() && pattern.getTagCompound().hasKey(NBT_OREDICT) && pattern.getTagCompound().getBoolean(NBT_OREDICT);
    }

    public static void setOredict(ItemStack pattern, boolean oredict) {
        if (!pattern.hasTagCompound()) {
            pattern.setTagCompound(new NBTTagCompound());
        }

        pattern.getTagCompound().setBoolean(NBT_OREDICT, oredict);
    }

    public static void setType(ItemStack pattern, RecipeType type) {
        if(!pattern.hasTagCompound()) {
            pattern.setTagCompound(new NBTTagCompound());
        }

        pattern.getTagCompound().setInteger(NBT_TYPE, type.ordinal());
    }

    public static RecipeType getType(ItemStack pattern) {
        return pattern.hasTagCompound() && pattern.getTagCompound().hasKey(NBT_TYPE) ? RecipeType.values()[pattern.getTagCompound().getInteger(NBT_TYPE)] : null;
    }

    public static void combineItems(List<String> tooltip, boolean displayAmount, NonNullList<ItemStack> stacks) {
        Set<Integer> combinedIndices = new HashSet<>();

        for (int i = 0; i < stacks.size(); ++i) {
            if (!stacks.get(i).isEmpty() && !combinedIndices.contains(i)) {
                ItemStack stack = stacks.get(i);

                String data = stack.getDisplayName();

                int amount = stack.getCount();

                for (int j = i + 1; j < stacks.size(); ++j) {
                    if (API.instance().getComparer().isEqual(stack, stacks.get(j))) {
                        amount += stacks.get(j).getCount();

                        combinedIndices.add(j);
                    }
                }

                data = (displayAmount ? (TextFormatting.WHITE + String.valueOf(amount) + " ") : "") + TextFormatting.GRAY + data;

                tooltip.add(data);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(!worldIn.isRemote && playerIn.isSneaking())
            return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(Registry.PATTERN, playerIn.getHeldItem(handIn).getCount()));
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Nonnull
    @Override
    public ICraftingPattern create(World world, ItemStack stack, ICraftingPatternContainer container) {
        return new ExtremePattern(world, container, stack);
    }
}
