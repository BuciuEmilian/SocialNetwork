package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.FriendDTO;
import com.example.socialnetwork.service.Service;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;

public abstract class RequestController implements Observer {
    protected Service service;
    protected ObservableList<FriendDTO> model = FXCollections.observableArrayList();
    @FXML
    protected TableView<FriendDTO> tableView;
    @FXML
    protected TableColumn<FriendDTO, String> usernameColumn;
    @FXML
    protected TableColumn<FriendDTO, String> firstNameColumn;
    @FXML
    protected TableColumn<FriendDTO, String> lastNameColumn;
    @FXML
    protected TableColumn<FriendDTO, LocalDateTime> dateTimeColumn;

    public void setService(Service service) {
        this.service = service;
        initModel();
        service.addObserver(this);
    }

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("username"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("lastName"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<FriendDTO, LocalDateTime>("dateTime"));
        tableView.setItems(model);
    }
    abstract protected void initModel();

    @Override
    public void update() {
        initModel();
    }
}
