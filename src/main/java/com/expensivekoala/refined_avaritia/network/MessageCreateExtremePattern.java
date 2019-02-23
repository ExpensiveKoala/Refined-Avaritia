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
public class MessageCreateExtremePattern extends Message<MessageCreateExtremePattern> implements IMessage {

    private int x;
    private int y;
    private int z;

    public MessageCreateExtremePattern() {

    }

    public MessageCreateExtremePattern(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    @Override
    public void handle(MessageCreateExtremePattern message, EntityPlayerMP player) {
        BlockPos pos = new BlockPos(message.x, message.y, message.z);
        if(player.getEntityWorld().isBlockLoaded(pos)) {
            TileEntity tile = player.getEntityWorld().getTileEntity(pos);

            if (tile instanceof TileExtremePatternEncoder)
                ((TileExtremePatternEncoder) tile).onCreatePattern();
        }
    }
}
