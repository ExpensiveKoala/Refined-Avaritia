package com.expensivekoala.refined_avaritia.network;

import com.expensivekoala.refined_avaritia.inventory.ItemHandlerRestricted;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
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

    public MessageTransferAvaritiaRecipe() {
    }

    public MessageTransferAvaritiaRecipe(int x, int y, int z, NonNullList<ItemStack> items) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.items = items;
    }

    @Override
    public void handle(MessageTransferAvaritiaRecipe message, EntityPlayerMP player) {
        TileEntity tile = player.getEntityWorld().getTileEntity(new BlockPos(message.x, message.y, message.z));

        if(tile instanceof TileExtremePatternEncoder) {
            TileExtremePatternEncoder encoder = (TileExtremePatternEncoder)tile;
            ItemHandlerRestricted recipe = (ItemHandlerRestricted)encoder.getRecipe();
            for (int i = 0; i < recipe.getSlots(); i++) {
                recipe.setStackInSlot(i, message.items.get(i));
            }
            ((TileExtremePatternEncoder) tile).onContentsChanged();
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
