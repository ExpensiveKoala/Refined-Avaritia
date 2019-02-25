package com.expensivekoala.refined_avaritia.item;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.Registry;
import com.expensivekoala.refined_avaritia.util.ExtremePattern;
import com.expensivekoala.refined_avaritia.util.RecipeType;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import com.raoulvdberge.refinedstorage.apiimpl.API;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ItemExtremePattern extends Item implements ICraftingPatternProvider {

    /**
     * Blatantly copying Refined Storage <3
     */
    private static Map<ItemStack, ExtremePattern> PATTERN_CACHE = new HashMap<>();

    private static final String NBT_SLOT = "Input_%d";
    private static final String NBT_OREDICT = "Oredict";
    public static final String NBT_TYPE = "Type";

    public ItemExtremePattern() {
        setRegistryName(RefinedAvaritia.MODID, "extreme_pattern");
        setCreativeTab(RefinedAvaritia.instance.tab);
    }

    @Override
    public String getTranslationKey() {
        return "item." + RefinedAvaritia.MODID + ":extreme_pattern";
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return getTranslationKey();
    }

    public static ExtremePattern getPatternFromCache(World world, ItemStack stack) {
        if (!PATTERN_CACHE.containsKey(stack)) {
            PATTERN_CACHE.put(stack, new ExtremePattern(world, null, stack));
        }
        return PATTERN_CACHE.get(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        ICraftingPattern pattern = getPatternFromCache(worldIn, stack);

        if (pattern.isValid()) {
            if (GuiScreen.isShiftKeyDown()) {
                tooltip.add(TextFormatting.YELLOW + I18n.format("misc.refinedstorage:pattern.inputs") + TextFormatting.RESET);

                combineItems(tooltip, true, pattern.getInputs().stream().map(i -> i.size() > 0 ? i.get(0) : ItemStack.EMPTY).collect(Collectors.toList()));

                tooltip.add(TextFormatting.YELLOW + I18n.format("misc.refinedstorage:pattern.outputs") + TextFormatting.RESET);
            }

            combineItems(tooltip, true, pattern.getOutputs());

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
        String idLegacy = String.format("Slot_%d", slot);

        if (!pattern.hasTagCompound()) {
            return ItemStack.EMPTY;
        }
        if (!pattern.getTagCompound().hasKey(idLegacy) && !pattern.getTagCompound().hasKey(id)) {
            return ItemStack.EMPTY;
        }
        if (pattern.getTagCompound().hasKey(idLegacy)) {
            return new ItemStack(pattern.getTagCompound().getCompoundTag(idLegacy));
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
        if (!pattern.hasTagCompound()) {
            pattern.setTagCompound(new NBTTagCompound());
        }

        pattern.getTagCompound().setInteger(NBT_TYPE, type.ordinal());
    }

    public static RecipeType getType(ItemStack pattern) {
        return pattern.hasTagCompound() && pattern.getTagCompound().hasKey(NBT_TYPE) ? RecipeType.values()[pattern.getTagCompound().getInteger(NBT_TYPE)] : null;
    }


    public static void combineItems(List<String> tooltip, boolean displayAmount, List<ItemStack> stacks) {
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

                data = TextFormatting.GRAY + (displayAmount ? (String.valueOf(amount) + "x ") : "") + data;

                tooltip.add(data);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote && playerIn.isSneaking()) {
            return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(Registry.PATTERN, playerIn.getHeldItem(handIn).getCount()));
        } else if (!worldIn.isRemote && playerIn.getHeldItem(handIn).hasTagCompound()) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            setOredict(stack, !isOredict(stack));
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Nonnull
    @Override
    public ICraftingPattern create(World world, ItemStack stack, ICraftingPatternContainer container) {
        return new ExtremePattern(world, container, stack);
    }
}
