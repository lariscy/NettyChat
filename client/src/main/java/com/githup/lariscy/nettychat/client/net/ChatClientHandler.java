package com.githup.lariscy.nettychat.client.net;

import com.githup.lariscy.nettychat.client.ChatClient;
import com.githup.lariscy.nettychat.client.event.IncomingChatEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javax.inject.Inject;
import net.engio.mbassy.bus.MBassador;

/**
 * @author Steven
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<Object> {
    
    @Inject
    private MBassador eventBus;
    private NettyClient nettyClient;

    public ChatClientHandler(NettyClient nettyClient) {
        ChatClient.getInjector().injectMembers(this);
        this.nettyClient = nettyClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object message) throws Exception {
        //super.channelRead(ctx, message);
        System.out.println("eventBus: "+eventBus);
        eventBus.publishAsync(new IncomingChatEvent(new ChatMessage((String) message)));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        nettyClient.setChannel(ctx.channel());
    }

}
