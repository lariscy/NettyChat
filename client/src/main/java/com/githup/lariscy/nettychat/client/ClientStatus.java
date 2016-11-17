package com.githup.lariscy.nettychat.client;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author Steven
 */
public class ClientStatus {
    
    private final BooleanProperty connected = new SimpleBooleanProperty(false);
    
    public boolean isConnected(){
        return connected.getValue();
    }
    
    public void setConnected(boolean connectedStatus){
        connected.setValue(connectedStatus);
    }
    
    public BooleanProperty getConnectedProperty(){
        return connected;
    }

}
