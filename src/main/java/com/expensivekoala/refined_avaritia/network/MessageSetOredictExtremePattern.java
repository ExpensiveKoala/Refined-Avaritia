package com.expensivekoala.refined_avaritia.network;

import com.expensivekoala.refined_avaritia.tile.TileExtremePatternEncoder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @author ExpensiveKoala
 */
public class MessageSetOredictExtremePattern extends Message<MessageSetOredictExtremePattern> implements IMessage {

    private int x, y, z;
    private boolean value;

    public MessageSetOredictExtremePattern() {

    }

    public MessageSetOredictExtremePattern(int x, int y, int z, boolean value) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.value = value;
    }

    @Override
    public void handle(MessageSetOredictExtremePattern message, EntityPlayerMP player) {
        TileEntity tile = player.getEntityWorld().getTileEntity(new BlockPos(message.x, message.y, message.z));

        if(tile instanceof TileExtremePatternEncoder) {
            ((TileExtremePatternEncoder) tile).setOredictPattern(message.value);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

}
