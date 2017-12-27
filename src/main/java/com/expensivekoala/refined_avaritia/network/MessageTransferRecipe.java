package com.expensivekoala.refined_avaritia.network;

import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import com.raoulvdberge.refinedstorage.inventory.ItemHandlerBase;
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
public class MessageTransferRecipe extends Message<MessageTransferRecipe> implements IMessage {

    private int x, y, z;
    private NonNullList<ItemStack> items;

    public MessageTransferRecipe() {
    }

    public MessageTransferRecipe(int x, int y, int z, NonNullList<ItemStack> items) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.items = items;
    }

    @Override
    public void handle(MessageTransferRecipe message, EntityPlayerMP player) {
        TileEntity tile = player.getEntityWorld().getTileEntity(new BlockPos(message.x, message.y, message.z));

        if(tile instanceof TileExtremePatternEncoder) {
            TileExtremePatternEncoder encoder = (TileExtremePatternEncoder)tile;
            ItemHandlerBase recipe = (ItemHandlerBase)encoder.getRecipe();
            for (int i = 0; i < recipe.getSlots(); i++) {
                recipe.setStackInSlot(i, message.items.get(i));
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        if(items == null) {
            items = NonNullList.withSize(81, ItemStack.EMPTY);
        }
        for (int i = 0; i < 81; i++) {
            items.set(i, ByteBufUtils.readItemStack(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        for (int i = 0; i < 81; i++) {
            ByteBufUtils.writeItemStack(buf, items.get(i));
        }
    }
}
