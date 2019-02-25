package com.expensivekoala.refined_avaritia.client;

import com.expensivekoala.refined_avaritia.item.ItemExtremePattern;
import com.expensivekoala.refined_avaritia.util.ExtremePattern;
import com.raoulvdberge.refinedstorage.api.autocrafting.ICraftingPatternRenderHandler;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BakedExtremePatternModel implements IBakedModel {
    private IBakedModel base;

    public BakedExtremePatternModel(IBakedModel model) {
        this.base = model;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return base.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return base.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return base.isAmbientOcclusion();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return base.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new ItemOverrideList(base.getOverrides().getOverrides()) {
            @Override
            public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                ExtremePattern pattern = ItemExtremePattern.getPatternFromCache(world, stack);

                if (canDisplayOutput(stack, pattern)) {
                    return Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(pattern.getOutputs().get(0), world, entity);
                }

                return super.handleItemState(originalModel, stack, world, entity);
            }
        };
    }

    public static boolean canDisplayOutput(ItemStack patternStack, ExtremePattern pattern) {
        if (pattern.isValid() && pattern.getOutputs().size() == 1) {
            for (ICraftingPatternRenderHandler renderHandler : API.instance().getPatternRenderHandlers()) {
                if (renderHandler.canRenderOutput(patternStack)) {
                    return true;
                }
            }
        }

        return false;
    }
}
