package com.githup.lariscy.nettychat.client.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.Future;

/**
 * @author Steven
 */
public class NettyClient {
    
    private static final String HOST = "localhost";
    private static final int PORT = 8383;
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Bootstrap b;
    private ChannelHandlerContext ctx;
    private boolean isConnected;
    private NettyClient instance;
    
    public NettyClient(){
        instance = this;
        this.setup();
    }
    
    private void setup(){
        b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(
                    new ObjectEncoder(),
                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                    new ChatClientHandler(instance)
                );
            }
        });
    }
    
    public boolean connect(){
        ChannelFuture connectFuture = b.connect(HOST, PORT);
        connectFuture.awaitUninterruptibly();
        if (connectFuture.isSuccess()){
            isConnected = true;
           return true; 
        } else {
            connectFuture.cause().printStackTrace();
        }
        return false;
    }
    
    public boolean disconnect(){
        ChannelFuture disConnectFuture = ctx.channel().disconnect();
        disConnectFuture.awaitUninterruptibly();
        if (disConnectFuture.isSuccess()){
            isConnected = false;
            return true;
        } else {
            disConnectFuture.cause().printStackTrace();
        }
        return false;
    }
    
    public boolean closeThreads(){
        Future closeThreadsFuture = workerGroup.shutdownGracefully();
        closeThreadsFuture.awaitUninterruptibly();
        if (closeThreadsFuture.isSuccess()){
            return true;
        } else {
            closeThreadsFuture.cause().printStackTrace();
        }
        return false;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return ctx;
    }
    
    public void setChannelHandlerContext(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }
    
    public boolean isConnected(){
        return isConnected;
    }

}
