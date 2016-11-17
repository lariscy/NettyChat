package com.githup.lariscy.nettychat.server;

import com.githup.lariscy.nettychat.server.net.NettyServer;

/**
 * @author Steven Lariscy
 */
public class ChatServer {
    
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.bind();
    }
    
}
