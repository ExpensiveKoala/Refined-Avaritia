package com.expensivekoala.refined_avaritia.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class Message<T extends IMessage> implements IMessageHandler<T, IMessage> {

    @Override
    public IMessage onMessage(T message, MessageContext context) {
        final EntityPlayerMP player = context.getServerHandler().playerEntity;

        player.getServerWorld().addScheduledTask(() -> handle(message, player));
        return null;
    }

    public abstract void handle(T message, EntityPlayerMP player);
}
