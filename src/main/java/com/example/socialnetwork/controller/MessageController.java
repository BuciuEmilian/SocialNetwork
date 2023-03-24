package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.service.Service;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class MessageController implements Observer {
    private Service service;
    private ObservableList<String> modelSent = FXCollections.observableArrayList();
    private ObservableList<String> modelReceived = FXCollections.observableArrayList();
    @FXML
    private ListView<String> sentListView;
    @FXML
    private ListView<String> receivedListView;
    @FXML
    private TextField messageField;
    private Long other;

    public void setService(Service service, Long other) {
        this.service = service;
        this.other = other;
        initModel();
        service.addObserver(this);
    }

    @FXML
    public void initialize() {
        sentListView.setItems(modelSent);
        receivedListView.setItems(modelReceived);
    }

    private void initModel() {
        List<String> sentMessages = service.getSentMessages(other);
        modelSent.setAll(sentMessages);

        List<String> receivedMessages = service.getReceivedMessages(other);
        modelReceived.setAll(receivedMessages);
    }

    public void handleSend(ActionEvent actionEvent) {
        try {
            String msg = messageField.getText();
            if (!msg.isEmpty()) {
                this.service.sendMessage(other, msg);
                messageField.clear();
            }
        } catch (Exception e) {
            // TODO:
        }
    }

    @Override
    public void update() {
        initModel();
    }
}
