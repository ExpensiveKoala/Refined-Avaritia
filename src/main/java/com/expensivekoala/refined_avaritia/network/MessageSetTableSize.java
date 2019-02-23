package com.expensivekoala.refined_avaritia.network;

import com.expensivekoala.refined_avaritia.gui.ContainerExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import com.expensivekoala.refined_avaritia.util.ExtendedCraftingUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @author ExpensiveKoala
 */
public class MessageSetTableSize extends Message<MessageSetTableSize> implements IMessage{

    private int x, y, z;
    private ExtendedCraftingUtil.TableSize size;

    public MessageSetTableSize() {
        size = ExtendedCraftingUtil.TableSize.BASIC;
    }

    public MessageSetTableSize(int x, int y, int z, ExtendedCraftingUtil.TableSize size) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
    }

    @Override
    public void handle(MessageSetTableSize message, EntityPlayerMP player) {
        BlockPos pos = new BlockPos(message.x, message.y, message.z);
        if(player.getEntityWorld().isBlockLoaded(pos)) {
            TileEntity tile = player.getEntityWorld().getTileEntity(pos);

            if (tile instanceof TileExtremePatternEncoder) {
                ((TileExtremePatternEncoder) tile).setTableSize(message.size);
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.size = ExtendedCraftingUtil.TableSize.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(size.ordinal());
    }
}
