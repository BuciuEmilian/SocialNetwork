package com.example.socialnetwork.controller;

import com.example.socialnetwork.HelloApplication;
import com.example.socialnetwork.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController {
    Service service;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    public void handleSinginButtonAction(ActionEvent actionEvent) {
        try {
            service.addUser(firstNameField.getText(), lastNameField.getText(), usernameField.getText(), passwordField.getText());
            service.login(usernameField.getText(), passwordField.getText());
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/socialnetwork/views/user-view.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                UserController userController = fxmlLoader.getController();
                userController.setService(service);
                stage.show();
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            } catch (Exception e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        catch (Exception e) {
            MessageAlert.showErrorMessage(null, "Username is already taken!");
        }
    }

}
