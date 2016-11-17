package com.githup.lariscy.nettychat.server.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Steven Lariscy
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<Object> {
    
    private NettyServer nettyServer;
    
    public ChatServerHandler(NettyServer nettyServer){
        this.nettyServer = nettyServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object message) throws Exception {
        System.out.println("recevied a message");
        nettyServer.sendMessage(message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        nettyServer.removeChannel(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        nettyServer.addChannel(ctx.channel());
        super.channelActive(ctx);
    }
    
}
