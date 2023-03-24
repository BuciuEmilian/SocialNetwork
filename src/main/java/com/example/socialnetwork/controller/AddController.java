package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.service.Service;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AddController implements Observer {
    private Service service;
    private ObservableList<User> model = FXCollections.observableArrayList();
    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User,String> lastNameColumn;

    public void setService(Service service) {
        this.service = service;
        initModel();
        service.addObserver(this);
    }

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));

        tableView.setItems(model);
    }
    private void initModel() {
        List<User> nonFriends = service.findAllNonFriends(this.service.getCurrentUserId());
        model.setAll(nonFriends);
    }

    @Override
    public void update() {
        initModel();
    }

    public void handleAdd(ActionEvent actionEvent) {
        try {
            User selected = tableView.getSelectionModel().getSelectedItem();
            Long id = selected.getId();
            service.addFriend(id);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "INFO", "Friend request has been sent!");
        }
        catch(Exception e) {
            // TODO:
        }

    }
}
