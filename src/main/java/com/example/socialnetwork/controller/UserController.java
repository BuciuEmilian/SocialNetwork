package com.example.socialnetwork.controller;

import com.example.socialnetwork.HelloApplication;
import com.example.socialnetwork.domain.FriendDTO;
import com.example.socialnetwork.service.Service;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer {
    private Service service;
    private ObservableList<FriendDTO> model = FXCollections.observableArrayList();

    @FXML
    private Label usernameLabel;
    @FXML
    private TableView<FriendDTO> tableView;
    @FXML
    private TableColumn<FriendDTO, String> usernameColumn;
    @FXML
    private TableColumn<FriendDTO, String> firstNameColumn;
    @FXML
    private TableColumn<FriendDTO, String> lastNameColumn;
    @FXML
    private TableColumn<FriendDTO, LocalDateTime> dateTimeColumn;

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

    private void initModel() {
        usernameLabel.setText(this.service.getCurrentUsername());
        Iterable<FriendDTO> friends = service.findAllFriends(this.service.getCurrentUserId());
        List<FriendDTO> friendsList = StreamSupport.stream(friends.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(friendsList);
    }

    @Override
    public void update() {
        initModel();
    }

    public void handleAdd(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/socialnetwork/views/add-view.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add page");

        AddController addController = fxmlLoader.getController();
        addController.setService(service);

        stage.show();
    }

    public void handleDelete(ActionEvent actionEvent) {
        try {
            FriendDTO selected = tableView.getSelectionModel().getSelectedItem();
            Long id = selected.getId();
            service.removeFriend(id);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "INFO", "Friend has been removed!");
            //initModel();
        }
        catch (Exception e) {
            // TODO:
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "ERROR", "You must select a user first!");
        }
    }

    public void handleChat(ActionEvent actionEvent) throws IOException {
        try {
            FriendDTO selected = tableView.getSelectionModel().getSelectedItem();
            Long id = selected.getId();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/socialnetwork/views/chat-view.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Chat page");

            MessageController messageController = fxmlLoader.getController();
            messageController.setService(service, id);

            stage.show();
        }
        catch (Exception e) {
            // TODO:
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "ERROR", "You must select a user first!");
        }
    }

    public void handleLogout(ActionEvent actionEvent) throws IOException {
        service.logout();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/socialnetwork/views/start-view.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Start page");
        StartController startController = fxmlLoader.getController();

        startController.setService(service);
        stage.show();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void handleReceivedFriendRequests(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/socialnetwork/views/received-request-view.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Received friend requests page");
        ReceivedRequestController requestController = fxmlLoader.getController();

        requestController.setService(service);
        stage.show();
    }

    public void handleSentFriendRequests(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/socialnetwork/views/sent-request-view.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Sent friend requests page");
        SentRequestController requestController = fxmlLoader.getController();

        requestController.setService(service);
        stage.show();
    }
}
