package com.expensivekoala.refined_avaritia.item;

import com.expensivekoala.refined_avaritia.RAItems;
import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.util.ExtremePattern;
import com.google.common.collect.Iterables;
import com.raoulvdberge.refinedstorage.RS;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.*;

public class ItemExtremePattern extends Item implements ICraftingPatternProvider{

    /**
     * Blatantly copying Refined Storage <3
     */
    private static Map<ItemStack, ExtremePattern> PATTERN_CACHE = new HashMap<>();

    private static final String NBT_SLOT = "Slot_%d";
    private static final String NBT_OREDICT = "Oredict";

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
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if(!stack.hasTagCompound()) {
            return;
        }

        ICraftingPattern pattern = getPatternFromCache(playerIn.getEntityWorld(), stack);
        if(pattern.isValid()) {
            if (GuiScreen.isShiftKeyDown()) {
                tooltip.add(TextFormatting.fromColorIndex((int)(playerIn.getEntityWorld().getWorldTime() % 15)) + I18n.format("misc.refinedstorage:pattern.inputs") + TextFormatting.RESET);

                combineItems(tooltip, true, Iterables.toArray(pattern.getInputs(), ItemStack.class));

                tooltip.add(TextFormatting.fromColorIndex((int)(playerIn.getEntityWorld().getWorldTime() % 15)) + I18n.format("misc.refinedstorage:pattern.outputs") + TextFormatting.RESET);
            }

            combineItems(tooltip, true, Iterables.toArray(pattern.getOutputs(), ItemStack.class));
        }
    }

    public static void setSlot(ItemStack pattern, int slot, ItemStack stack) {
        // Safety against bad stacks
        if (stack.stackSize < 0) {
            stack.stackSize = 1;
        }

        if (!pattern.hasTagCompound()) {
            pattern.setTagCompound(new NBTTagCompound());
        }

        pattern.getTagCompound().setTag(String.format(NBT_SLOT, slot), stack.serializeNBT());
    }

    public static ItemStack getSlot(ItemStack pattern, int slot) {
        String id = String.format(NBT_SLOT, slot);

        if (!pattern.hasTagCompound() || !pattern.getTagCompound().hasKey(id)) {
            return null;
        }

        return ItemStack.loadItemStackFromNBT(pattern.getTagCompound().getCompoundTag(id));
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

    public static void combineItems(List<String> tooltip, boolean displayAmount, ItemStack... stacks) {
        Set<Integer> combinedIndices = new HashSet<>();

        for (int i = 0; i < stacks.length; ++i) {
            if (stacks[i] != null && !combinedIndices.contains(i)) {
                String data = stacks[i].getDisplayName();

                int amount = stacks[i].stackSize;

                for (int j = i + 1; j < stacks.length; ++j) {
                    if (API.instance().getComparer().isEqual(stacks[i], stacks[j])) {
                        amount += stacks[j].stackSize;

                        combinedIndices.add(j);
                    }
                }

                data = (displayAmount ? (TextFormatting.WHITE + String.valueOf(amount) + " ") : "") + TextFormatting.GRAY + data;

                tooltip.add(data);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if(!worldIn.isRemote && playerIn.isSneaking())
            return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(RAItems.PATTERN, itemStackIn.stackSize));
        return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
    }

    @Nonnull
    @Override
    public ICraftingPattern create(World world, ItemStack stack, ICraftingPatternContainer container) {
        return new ExtremePattern(world, container, stack);
    }
}
