package com.example.socialnetwork.ui;

import com.example.socialnetwork.domain.FriendDTO;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.domain.validator.ValidationException;
import com.example.socialnetwork.service.Service;

import java.util.Scanner;

public class Gui {
    public final Service service;
    Scanner in;

    public Gui(Service service) {
        this.service = service;
        this.in = new Scanner(System.in);
    }

    private void printCommands() {
        System.out.println();
        System.out.printf("Current user: %s\n\n", this.service.getCurrentUsername());
        System.out.println("Commands:");
        System.out.println("stop");
        System.out.println("1. Login");
        System.out.println("2. Add user");
        System.out.println("3. Remove user");
        System.out.println("4. [Unavailable] Update user");
        System.out.println("5. Print friends");
        System.out.println("6. Send friend request");
        System.out.println("7. Remove friend");
        System.out.println("8. Print friend requests");
        System.out.println("9. Accept friend request");
        System.out.println("10. Reject friend request");
        System.out.println("11. Print the number of communities");
        System.out.println("12. [Unavailable] Print the most social community");
        System.out.println("13. Print all");
    }

    public void start() {
        boolean running = true;
        User user = this.service.findUserById(11L);
        String username = user.getUsername();
        String password = user.getPassword();
        this.service.login(username, password);
        while (running) {
            try {
                this.printCommands();
                String input = in.nextLine();
                switch (input) {
                    case "stop" -> running = false;
                    case "1" -> this.login();
                    case "2" -> this.addUser();
                    case "3" -> this.removeUser();
                    case "4" -> this.updateUser();
                    case "5" -> this.printFriends();
                    case "6" -> this.addFriend();
                    case "7" -> this.removeFriend();
                    case "8" -> this.printFriendRequests();
                    case "9" -> this.acceptFriendRequest();
                    case "10" -> this.rejectFriendRequest();
                    case "11" -> this.printNumberOfCommunities();
                    case "12" -> this.printMostSocialCommunity();
                    case "13" -> this.printAll();
                    default -> System.out.println("Incorrect command.");
                }
            }
            catch (IllegalArgumentException | ValidationException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void printAll() {
        System.out.println("Users:\n");
        Iterable<User> allUsers = this.service.findAllUsers();
        for (User user : allUsers)
            System.out.println(user);

        System.out.println();

        System.out.println("Friendships:\n");
        Iterable<Friendship> allFriendships = this.service.findAllFriendships();
        for (Friendship friendship : allFriendships)
            System.out.println(friendship);

        System.out.println();
    }

    private void printFriends() {
        System.out.println("Friends:\n");
        Iterable<FriendDTO> allFriends = this.service.findAllFriends(this.service.getCurrentUserId());
        for (FriendDTO friend : allFriends)
            System.out.println(friend);

        System.out.println();
    }

    private void printFriendRequests() {
        System.out.println("Friend requests:\n");
        Iterable<FriendDTO> allFriendRequests = this.service.findAllReceivedFriendRequests(this.service.getCurrentUserId());

        allFriendRequests.forEach(System.out::println);

        System.out.println();
    }

    private void login() {
        System.out.println("Username: ");
        String username = in.nextLine();
        System.out.println("Password: ");
        String password = in.nextLine();

        this.service.login(username, password);
    }

    private void addUser() {
        System.out.println("First name: ");
        String firstName = in.nextLine();
        System.out.println("Last name: ");
        String lastName = in.nextLine();
        System.out.println("Username: ");
        String username = in.nextLine();
        System.out.println("Password: ");
        String password = in.nextLine();

        this.service.addUser(firstName, lastName, username, password);
    }

    private void removeUser() {
        this.service.removeUser();
    }

    private void updateUser() {
        // TODO:
    }

    private void addFriend() {
        System.out.println("Id: ");
        Long id = in.nextLong();

        this.service.addFriend(id);
    }

    private void removeFriend() {
        System.out.println("Username: ");
        Long id = in.nextLong();

        this.service.removeFriend(id);
    }

    private void acceptFriendRequest() {
        System.out.println("Id: ");
        Long id = in.nextLong();

        this.service.acceptFriendRequest(id);
    }

    private void rejectFriendRequest() {
        System.out.println("Id: ");
        Long id = in.nextLong();

        this.service.rejectFriendRequest(id);
    }

    private void printNumberOfCommunities() {
        System.out.println(this.service.getNumberOfCommunities());
    }

    private void printMostSocialCommunity() {
        // TODO:
    }
}