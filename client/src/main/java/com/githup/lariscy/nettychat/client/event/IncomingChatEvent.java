package com.githup.lariscy.nettychat.client.event;

import com.githup.lariscy.nettychat.shared.ChatMessage;

/**
 * @author Steven
 */
public class IncomingChatEvent {
    
    private ChatMessage chatMessage;
    
    public IncomingChatEvent(ChatMessage chatMessage){
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

}
