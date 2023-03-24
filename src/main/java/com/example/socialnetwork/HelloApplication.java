package com.example.socialnetwork;

import com.example.socialnetwork.controller.StartController;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.domain.Pair;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validator.FriendshipValidator;
import com.example.socialnetwork.domain.validator.UserValidator;
import com.example.socialnetwork.domain.validator.Validator;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.repository.db.DataBaseMessagesRepository;
import com.example.socialnetwork.repository.db.DataBaseUserRepository;
import com.example.socialnetwork.repository.db.DataBaseFriendshipRepository;
import com.example.socialnetwork.repository.memory.LoginRepository;
import com.example.socialnetwork.service.Service;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    Service service;
    Repository<Long, User> userRepository;
    Validator<User> userValidator;
    Repository<Pair<Long>, Friendship> friendshipRepository;
    Validator<Friendship> friendshipValidator;;
    Repository<Long, Message> messageRepository;
    LoginRepository loginRepository;

    private void initView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/socialnetwork/views/start-view.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        stage.setScene(new Scene(userLayout));

        StartController startController = fxmlLoader.getController();
        startController.setService(service);
    }

    @Override
    public void start(Stage stage) throws IOException {
        String url = "jdbc:sqlite:D:\\Facultate\\Sem3\\MAP\\Lab\\SocialNetwork\\socialnetwork.db";
        String usernameDB = "";
        String passwordDB = "";
        this.userRepository = new DataBaseUserRepository(
                url,
                usernameDB,
                passwordDB
        );
        this.userValidator = new UserValidator();

        this.friendshipRepository = new DataBaseFriendshipRepository(
                url,
                usernameDB,
                passwordDB
        );
        this.friendshipValidator = new FriendshipValidator();

        this.messageRepository = new DataBaseMessagesRepository(
                url,
                usernameDB,
                passwordDB
        );

        this.loginRepository = new LoginRepository();

        this.service = new Service(
                userRepository,
                userValidator,
                friendshipRepository,
                messageRepository,
                friendshipValidator,
                loginRepository
        );
        initView(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}