package com.githup.lariscy.nettychat.client.service;

import com.githup.lariscy.nettychat.shared.ChatMessage;
import com.githup.lariscy.nettychat.client.net.NettyClient;
import io.netty.channel.ChannelFuture;

/**
 * @author Steven
 */
public class NetworkService {
    
    private NettyClient nettyClient = new NettyClient();
    
    public boolean connect(){
        return nettyClient.connect();
    }
    
    public boolean disconnect(){
        return nettyClient.disconnect();
    }
    
    public void sendMessage(String message){
        ChannelFuture sendFuture = nettyClient.getChannel().writeAndFlush(new ChatMessage(message));
        sendFuture.awaitUninterruptibly();
        if (!sendFuture.isSuccess()){
            sendFuture.cause().printStackTrace();
        }
    }
    
    public boolean isClientConnected(){
        return nettyClient.isConnected();
    }

}
