package com.githup.lariscy.nettychat.client.controller;

import com.githup.lariscy.nettychat.client.ClientStatus;
import com.githup.lariscy.nettychat.client.event.IncomingChatEvent;
import com.githup.lariscy.nettychat.client.service.NetworkService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.inject.Inject;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

/**
 * @author Steven
 */
public class ChatClientController implements Initializable {

    @FXML private TextArea txtMessageHistory;
    @FXML private TextField txtMessage;
    @FXML private Button btnSend;
    @FXML private Label lblStatus;
    @FXML private Button btnConnect;
    @FXML private Button btnDisconnect;
    
    private final ClientStatus clientStatus = new ClientStatus();
    private NetworkService networkService;
    private static final int NUM_CONNECT_RETRIES = 3;
    
    @Inject
    private MBassador eventBus;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.setupBindings();
        
        eventBus.subscribe(this);
    }
    
    private void setupBindings(){
        lblStatus.textProperty().bind(new StringBinding(){
            {
                super.bind(clientStatus.getConnectedProperty());
            }
            @Override
            protected String computeValue() {
                if (clientStatus.isConnected()){
                    return "Connected";
                }
                return "Disconnected";
            }
        });
        
        txtMessageHistory.textProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                txtMessageHistory.setScrollTop(Double.MAX_VALUE);
            }
        });
        
        txtMessage.disableProperty().bind(clientStatus.getConnectedProperty().not());
        btnSend.disableProperty().bind(clientStatus.getConnectedProperty().not());
        btnConnect.disableProperty().bind(clientStatus.getConnectedProperty());
        btnDisconnect.disableProperty().bind(clientStatus.getConnectedProperty().not());
    }
    
    public void setNetworkService(NetworkService networkService){
        this.networkService = networkService;
    }
    
    private void addMessageToHistory(String message){
        if (txtMessageHistory.getText().equals("")){
            txtMessageHistory.setText(message);
        } else {
            txtMessageHistory.setText(txtMessageHistory.getText()+'\n'+message);
        }
        txtMessageHistory.appendText("");
    }
    
    @FXML
    private void sendMessage(){
        if (txtMessage.getText().trim().equals(""))
            return;
        networkService.sendMessage(txtMessage.getText());
        //this.addMessageToHistory(txtMessage.getText());
        
        txtMessage.setText("");
    }
    
    private int connectAttempts;
    @FXML
    private void clientConnect(){
        new Thread(() -> {
            System.out.println("attempting to connect");
            connectAttempts = 1;
            for (connectAttempts = 1; connectAttempts<=3; connectAttempts++){
                if (networkService.connect() && networkService.isClientConnected()){
                    Platform.runLater(() -> {
                        addMessageToHistory("client connected succcessfully");
                        clientStatus.setConnected(true);
                    });
                    break;
                } else {
                    Platform.runLater(() ->
                            addMessageToHistory("unable to connect "+
                                    "["+connectAttempts+" of "+NUM_CONNECT_RETRIES+"], retrying in 3 seconds"));
                    try { Thread.sleep(3000);  } catch (InterruptedException ex) { }
                }
            }
        }).start();
    }
    
    @FXML
    private void clientDisconnect(){
        System.out.println("attempting to disconnect");
        if (networkService.disconnect()){
            System.out.println("disconnected");
            this.addMessageToHistory("client disconnected succcessfully");
            clientStatus.setConnected(false);
        } else {
            this.addMessageToHistory("unable to disconnect");
        }
    }
    
    @Handler
    private void messageReceived(IncomingChatEvent event){
        Platform.runLater(() ->
                this.addMessageToHistory(event.getChatMessage().getMessage()));
    }
    
}
