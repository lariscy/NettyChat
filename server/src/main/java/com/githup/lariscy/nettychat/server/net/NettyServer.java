package com.githup.lariscy.nettychat.server.net;

import com.githup.lariscy.nettychat.shared.ChatMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author Steven Lariscy
 */
public class NettyServer {
    
    private static final int PORT = 8383;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ServerBootstrap b;
    private NettyServer instance;
    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public NettyServer() {
        instance = this;
        this.setup();
    }
    
    private void setup(){
        System.out.println("setting up server");
        b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                    new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())),
                    new ObjectEncoder(),
                    new ChatServerHandler(instance)
                );
            }
        });
    }
    
    public void bind(){
        System.out.println("attempting to bind server to port "+PORT);
        Future bindFuture = b.bind(PORT);
        bindFuture.addListener((Future f) -> {
            if (f.isSuccess()){
                System.out.println("server now listening on port "+PORT);
            }
        });
    }
    
    public void shutdown(){
        System.out.println("server shutting down");
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
    
    public void addChannel(Channel channel){
        channelGroup.add(channel);
        System.out.println("channel ["+channel.remoteAddress()+"] added - current size: "+channelGroup.size());
    }
    
    public void removeChannel(Channel channel){
        channelGroup.remove(channel);
        System.out.println("channel ["+channel.remoteAddress()+"] removed - current size: "+channelGroup.size());
    }
    
    public void sendMessage(ChatMessage message){
        channelGroup.forEach((ch) -> 
            ch.writeAndFlush(message));
    }
    
}
