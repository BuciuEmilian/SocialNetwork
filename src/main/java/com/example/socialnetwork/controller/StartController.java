package com.example.socialnetwork.controller;

import com.example.socialnetwork.HelloApplication;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.function.Consumer;

public class StartController {
    Service service;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    public void setService(Service service) {
        this.service = service;
    }
    @FXML
    public void handleLoginButtonAction(ActionEvent actionEvent){
        try {
            //User user = this.service.findUserById(3L);
            //this.service.login(user.getUsername(), user.getPassword());

            service.login(usernameField.getText(), passwordField.getText());
            if (this.service.getCurrentUserId() != null) {
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/socialnetwork/views/user-view.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    UserController userController = fxmlLoader.getController();
                    userController.setService(this.service);
                    stage.show();
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            MessageAlert.showErrorMessage(null, "Wrong username or password!");
        }
    }

    @FXML
    public void handleSinginButtonAction(ActionEvent actionEvent){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/socialnetwork/views/signup-view.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Signup page");
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
            //System.out.println("HELLO");
            handleLoginButtonAction(new ActionEvent(loginButton, null));
        }
    }
}
