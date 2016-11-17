package com.githup.lariscy.nettychat.shared;

import java.io.Serializable;

/**
 * @author Steven Lariscy
 */
public class ChatMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String message;

    public ChatMessage() {
    }

    public ChatMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
