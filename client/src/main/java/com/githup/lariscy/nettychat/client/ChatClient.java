package com.githup.lariscy.nettychat.client;

import com.githup.lariscy.nettychat.client.controller.ChatClientController;
import com.githup.lariscy.nettychat.client.service.NetworkService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Steven
 */
public class ChatClient extends Application {
    
    private Stage primaryStage;
    private static Injector injector = Guice.createInjector(new ChatClientGuiceModule());
    private NetworkService networkService = new NetworkService();
    
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/githup/lariscy/nettychat/client/fxml/ChatClient.fxml"));
        loader.setControllerFactory(injector::getInstance);
        Parent parent = loader.load();
        ChatClientController controller = loader.getController();
        controller.setNetworkService(networkService);
        
        primaryStage.setTitle("NettyChat - Client");
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        networkService.disconnect();
        networkService.closeThreads();
    }
    
    public static Injector getInjector(){
        return injector;
    }

}
