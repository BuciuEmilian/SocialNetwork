package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.FriendDTO;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.List;

public class SentRequestController extends RequestController {
    @Override
    protected void initModel() {
        List<FriendDTO> friendRequests = service.findAllSentFriendRequests(service.getCurrentUserId());
        model.setAll(friendRequests);
    }
    public void handleDelete(ActionEvent actionEvent) {
        FriendDTO selected = tableView.getSelectionModel().getSelectedItem();
        Long id = selected.getId();
        service.rejectFriendRequest(id);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "INFO", "Friend request has been deleted!");
    }
}
