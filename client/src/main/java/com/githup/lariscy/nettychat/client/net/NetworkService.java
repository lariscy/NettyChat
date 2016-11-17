package com.githup.lariscy.nettychat.client.net;

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
        nettyClient.getChannel().writeAndFlush(new ChatMessage(message));
    }
    
    public boolean isClientConnected(){
        return nettyClient.isConnected();
    }

}
