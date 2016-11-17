package com.githup.lariscy.nettychat.client.net;

import com.githup.lariscy.nettychat.client.event.IncomingChatEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.engio.mbassy.bus.MBassador;

/**
 * @author Steven
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<ChatMessage> {
    
    private MBassador eventBus;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage message) throws Exception {
        super.channelRead(ctx, message);
        eventBus.publishAsync(new IncomingChatEvent(message));
    }

}
