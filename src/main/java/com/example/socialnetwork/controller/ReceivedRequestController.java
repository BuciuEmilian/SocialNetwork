package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.FriendDTO;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.List;

public class ReceivedRequestController extends RequestController {
    @Override
    protected void initModel() {
        List<FriendDTO> friendRequests = service.findAllReceivedFriendRequests(service.getCurrentUserId());
        model.setAll(friendRequests);
    }

    public void handleAccept(ActionEvent actionEvent) {
        FriendDTO selected = tableView.getSelectionModel().getSelectedItem();
        Long id = selected.getId();
        service.acceptFriendRequest(id);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "INFO", "Friend request has been accepted!");
    }

    public void handleReject(ActionEvent actionEvent) {
        FriendDTO selected = tableView.getSelectionModel().getSelectedItem();
        Long id = selected.getId();
        service.rejectFriendRequest(id);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "INFO", "Friend request has been rejected!");
    }
}
