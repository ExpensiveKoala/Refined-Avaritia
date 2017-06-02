package com.expensivekoala.refined_avaritia.block;

import com.expensivekoala.refined_avaritia.RefinedAvaritia;
import com.expensivekoala.refined_avaritia.gui.handlers.GuiHandler;
import com.expensivekoala.refined_avaritia.tile.TileExtremeCrafter;
import com.raoulvdberge.refinedstorage.RS;
import com.raoulvdberge.refinedstorage.block.BlockNode;
import com.raoulvdberge.refinedstorage.block.EnumPlacementType;
import com.raoulvdberge.refinedstorage.item.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockExtremeCrafter extends Block implements ITileEntityProvider{


    public BlockExtremeCrafter() {
        super(Material.ROCK);
        setHardness(1.9f);
        this.setRegistryName(RefinedAvaritia.MODID, "extreme_crafter");
        this.setCreativeTab(RS.INSTANCE.tab);
    }


    @Override
    public String getUnlocalizedName() {
        return "block." + RefinedAvaritia.MODID + ":extreme_crafter";
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileExtremeCrafter();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            playerIn.openGui(RefinedAvaritia.instance, GuiHandler.EXTREME_CRAFTER_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    public ItemBlock createItem() {
        return new ItemBlockBase(this, null, false);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileExtremeCrafter();
    }
}
