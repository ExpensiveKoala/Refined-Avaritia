package com.expensivekoala.refined_avaritia.network;

import com.expensivekoala.refined_avaritia.inventory.ItemHandlerRestricted;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.util.ExtendedCraftingUtil;
import com.expensivekoala.refined_avaritia.util.RecipeType;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @author ExpensiveKoala
 */
public class MessageTransferAvaritiaRecipe extends Message<MessageTransferAvaritiaRecipe> implements IMessage {

    private int x, y, z;
    private NonNullList<ItemStack> items;
    private RecipeType type;

    public MessageTransferAvaritiaRecipe() {
    }

    public MessageTransferAvaritiaRecipe(int x, int y, int z, NonNullList<ItemStack> items, RecipeType type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.items = items;
        this.type = type;
    }

    @Override
    public void handle(MessageTransferAvaritiaRecipe message, EntityPlayerMP player) {
        BlockPos pos = new BlockPos(message.x, message.y, message.z);
        if (player.getEntityWorld().isBlockLoaded(pos)) {
            TileEntity tile = player.getEntityWorld().getTileEntity(pos);

            if (tile instanceof TileExtremePatternEncoder) {
                TileExtremePatternEncoder encoder = (TileExtremePatternEncoder) tile;
                ItemHandlerRestricted recipe = (ItemHandlerRestricted) encoder.getRecipe();
                if (message.type == RecipeType.AVARITIA || message.type == RecipeType.EC_ULTIMATE) {
                    for (int i = 0; i < recipe.getSlots(); i++) {
                        recipe.setStackInSlot(i, message.items.get(i));
                    }
                    encoder.setTableSize(ExtendedCraftingUtil.TableSize.ULTIMATE);
                    encoder.setAvaritia(message.type == RecipeType.AVARITIA);
                } else {
                    for (int i = 0; i < message.items.size(); i++) {
                        int x = i % message.type.width;
                        int y = i / message.type.height;
                        recipe.setStackInSlot(((y + (9 - message.type.height) / 2) * 9) + (x + (9 - message.type.width) / 2), message.items.get(i));
                    }
                    encoder.setAvaritia(false);
                    switch (message.type) {
                        case EC_BASIC:
                            encoder.setTableSize(ExtendedCraftingUtil.TableSize.BASIC);
                            break;
                        case EC_ADVANCED:
                            encoder.setTableSize(ExtendedCraftingUtil.TableSize.ADVANCED);
                            break;
                        case EC_ELITE:
                            encoder.setTableSize(ExtendedCraftingUtil.TableSize.ELITE);
                            break;
                        case EC_ULTIMATE:
                            encoder.setTableSize(ExtendedCraftingUtil.TableSize.ULTIMATE);
                            break;
                    }
                }
                ((TileExtremePatternEncoder) tile).onContentsChanged();
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        type = RecipeType.values()[buf.readInt()];
        if (items == null) {
            items = NonNullList.withSize(type.width * type.height, ItemStack.EMPTY);
        }
        for (int i = 0; i < type.width * type.height; i++) {
            items.set(i, ByteBufUtils.readItemStack(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(type.ordinal());
        for (int i = 0; i < type.width * type.height; i++) {
            ByteBufUtils.writeItemStack(buf, items.get(i));
        }
    }
}
