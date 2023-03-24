package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.domain.validator.Validator;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.repository.memory.LoginRepository;
import com.example.socialnetwork.service.utils.Graph;
import com.example.socialnetwork.utils.observer.Observable;
import com.example.socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.socialnetwork.utils.Constants.DATE_TIME_FORMATTER;

public class Service implements Observable {
    private final Repository<Long, User> userRepository;
    private final Repository<Pair<Long>, Friendship> friendshipRepository;
    private final Repository<Long, Message> messageRepository;
    private final Validator<User> userValidator;
    private final Validator<Friendship> friendshipValidator;
    private final LoginRepository loginRepository;

    public Service(Repository<Long, User> userRepository,
                   Validator<User> userValidator,
                   Repository<Pair<Long>, Friendship> friendshipRepository,
                   Repository<Long, Message> messageRepository, Validator<Friendship> friendshipValidator,
                   LoginRepository loginRepository) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.friendshipRepository = friendshipRepository;
        this.messageRepository = messageRepository;
        this.friendshipValidator = friendshipValidator;
        this.loginRepository = loginRepository;
    }

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(o -> o.update());
    }

    /**
     *
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        User user = this.findUserByUsername(username);

        // there's no user with this username
        if (user == null) {
            // TODO: throw exception (something like WrongUsernameException)
            return;
        }

        if (user.getPassword().equals(password)) {
            this.loginRepository.login(user);
        }
        else {
            // TODO: throw exception (something like WrongPasswordException)
        }
    }

    public void logout() {
        this.loginRepository.logout();
    }

    public Long getCurrentUserId() {
        return this.loginRepository.getCurrentUserId();
    }

    public String getCurrentUsername() {
        return this.loginRepository.getCurrentUsername();
    }

    /**
     *
     * @param username - the username of the user to be searched
     * @return - null if there's user with this username, the user otherwise
     */
    public User findUserByUsername(String username) {
        return StreamSupport
                .stream(this.findAllUsers().spliterator(), false)
                .filter(user -> user.getUsername().equals(username))
                .toList()
                .get(0);
    }

    /**
     *
     * @param id - id of the users to be returned
     * @return the users with the given id (null if there's no user with that id)
     */
    public User findUserById(Long id) {
        return this.userRepository.findOne(id);
    }

    /**
     *
     * @return an Iterable with all the users in the network
     */
    public Iterable<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    /**
     *
     * @return an Iterable with all the friendships in the network
     */
    public Iterable<Friendship> findAllFriendships() {
        return this.friendshipRepository.findAll();
    }

    /**
     *
     * @return an Iterable with all the messages in the network
     */
    public Iterable<Message> findAllMessages() {
        return this.messageRepository.findAll();
    }

    /**
     *
     * @param id
     * @return a list of the friends the users with the given [id] has
     */
    public List<FriendDTO> findAllFriends(Long id) {
        return StreamSupport
                .stream(this.findAllFriendships().spliterator(), false)
                .filter(friendship -> friendship.getId().getFirst().equals(id) || friendship.getId().getSecond().equals(id))
                .filter(friendship -> friendship.getStatus().equals("accepted"))
                .map(friendship -> {
                    User user = null;
                    if (friendship.getId().getFirst().equals(id)) {
                        user = this.userRepository.findOne(friendship.getId().getSecond());
                    }
                    else {
                        user = this.userRepository.findOne(friendship.getId().getFirst());
                    }
                    return new FriendDTO(
                            user.getId(),
                            user.getUsername(),
                            user.getFirstName(),
                            user.getLastName(),
                            friendship.getFriendsFrom().format(DATE_TIME_FORMATTER)
                    );
                })
                .toList();
    }

    /**
     *
     * @param id
     * @return a list of all users that aren't friends with the user whose [id] is given
     */
    public List<User> findAllNonFriends(Long id) {

        return StreamSupport
                .stream(this.findAllUsers().spliterator(), false)
                .filter(user -> this.friendshipRepository.findOne(new Pair<>(user.getId(), id)) == null &&
                       !user.getId().equals(id))
                .toList();
    }

    /**
     *
     * @param id
     * @return the list of all received friend requests that the user with the given [id] has
     */
    public List<FriendDTO> findAllReceivedFriendRequests(Long id) {
        return StreamSupport
                .stream(this.findAllFriendships().spliterator(), false)
                .filter(friendship -> friendship.getStatus().equals("pending") && friendship.getId().getSecond().equals(id))
                .map(friendship -> {
                    User user = this.userRepository.findOne(friendship.getId().getFirst());
                    return new FriendDTO(
                            user.getId(),
                            user.getUsername(),
                            user.getFirstName(),
                            user.getLastName(),
                            friendship.getFriendsFrom().format(DATE_TIME_FORMATTER)
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     *
     * @param id
     * @return the list of all sent friend requests that the user with the given [id] has
     */
    public List<FriendDTO> findAllSentFriendRequests(Long id) {
        return StreamSupport
                .stream(this.findAllFriendships().spliterator(), false)
                .filter(friendship -> friendship.getStatus().equals("pending") && friendship.getId().getFirst().equals(id))
                .map(friendship -> {
                    User user = this.userRepository.findOne(friendship.getId().getSecond());
                    return new FriendDTO(
                            user.getId(),
                            user.getUsername(),
                            user.getFirstName(),
                            user.getLastName(),
                            friendship.getFriendsFrom().format(DATE_TIME_FORMATTER)
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     *
     * @param firstName - first name of the user to be added
     * @param lastName - last name of the user to be added
     * @param username - username of the user to be added (must be unique)
     * @param password - password of the user to be added
     */
    public void addUser(String firstName, String lastName, String username, String password) {
        User newUser = new User(firstName, lastName, username, password);
        this.userValidator.validate(newUser);

        // there's already a user with this username
        if (this.findUserByUsername(username) != null){
            // TODO : throw exception (something like InvalidUsernameException)
            return;
        }

        this.userRepository.save(newUser);
    }

    /**
     * deletes the current user from the network
     */
    public void removeUser() {
        this.userRepository.delete(this.loginRepository.getCurrentUserId());
    }

    /**
     *
     * @param id - id of the user to be updated (if it exists)
     * @param firstName - the new first name can remain unchanged) of the user to be updated
     * @param lastName - the new last name (can remain unchanged) of the user to be updated
     * @param username - the new username (can remain unchanged) of the user to be updated
     * @param password - the new password (can remain unchanged) of the user to be updated
     */
    public void updateUser(Long id, String firstName, String lastName, String username, String password) {
        User newUser = new User(firstName, lastName, username, password);
        newUser.setId(id);
        userValidator.validate(newUser);
        this.userRepository.update(newUser);
    }

    /**
     * @param id - the id of the user to be added as friend
     */
    public void addFriend(Long id) {
        Friendship newFriendship = new Friendship(LocalDateTime.now());
        newFriendship.setId(new Pair<>(this.loginRepository.getCurrentUserId(), id));
        this.friendshipValidator.validate(newFriendship);

        // pending request
        if (this.friendshipRepository.save(newFriendship) != null) {
            acceptFriendRequest(id);
        }

        notifyObservers();
    }

    /**
     *
     * removes a users from the friend list of the current user
     *
     * @param id - the id of the user to be removed from the friend list of the current user
     */
    public void removeFriend(Long id) {

        Friendship friendship = this.friendshipRepository.delete(new Pair<>(this.loginRepository.getCurrentUserId(), id));

        if (friendship == null) {
            // TODO : throw exception (something like InvalidUsernameException)
        }

        notifyObservers();
    }

    /**
     *
     * @param userId - id of the first user
     * @param friendId - id of the second user
     * @param status - the new status
     */
    public void updateFriendship(Long userId, Long friendId, String status) {
        if (status.equals("rejected")) {
            this.friendshipRepository.delete(new Pair<>(userId, friendId));
            return;
        }
        Friendship newFriendship = new Friendship(LocalDateTime.now());
        newFriendship.setStatus(status);
        newFriendship.setId(new Pair<>(userId, friendId));
        this.friendshipValidator.validate(newFriendship);
        this.friendshipRepository.update(newFriendship);
    }

    /**
     *
     * @param id - id of the user whose friend request we'll accept
     */
    public void acceptFriendRequest(Long id) {
        Long currentUserId = this.loginRepository.getCurrentUserId();

        this.updateFriendship(id, currentUserId, "accepted");

        notifyObservers();
    }

    /**
     *
     * @param id - id of the user whose friend request we'll reject
     */
    public void rejectFriendRequest(Long id) {
        Long currentUserId = this.loginRepository.getCurrentUserId();

        this.updateFriendship(id, currentUserId, "rejected");

        notifyObservers();
    }

    public void sendMessage(Long other, String body) {
        Long self = this.getCurrentUserId();
        Message newMessage = new Message(self, other, body, LocalDateTime.now());

        this.messageRepository.save(newMessage);

        notifyObservers();
    }

    public List<String> getSentMessages(Long other) {
        Long self = this.getCurrentUserId();
        return StreamSupport
                .stream(this.findAllMessages().spliterator(), false)
                .filter(message -> message.getFrom().equals(self) && message.getTo().equals(other) ||
                                    message.getFrom().equals(other) && message.getTo().equals(self))
                .map(message -> {
                    if (message.getFrom().equals(self) && message.getTo().equals(other)) {
                        return message.getBody();
                    }
                    else {
                        return "";
                    }
                })
                .collect(Collectors.toList());
    }

    public List<String> getReceivedMessages(Long other) {
        Long self = this.getCurrentUserId();
        return StreamSupport
                .stream(this.findAllMessages().spliterator(), false)
                .filter(message -> message.getFrom().equals(self) && message.getTo().equals(other) ||
                        message.getFrom().equals(other) && message.getTo().equals(self))
                .map(message -> {
                    if (message.getFrom().equals(self) && message.getTo().equals(other)) {
                        return "";
                    }
                    else {
                        return message.getBody();
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     *
     * @return the number of communities in the network
     */
    public int getNumberOfCommunities() {
        Iterable<User> allUsers = this.userRepository.findAll();

        // I calculate how long my ArrayList should be (could be solved by implementing the adjList using a HashMap)
        int maxId = -1;
        for (User user : allUsers) {
            maxId = Math.max(maxId, user.getId().intValue());
        }

        // I mark the nodes that actually exist in my network (waste of space, refactor as HashMap)
        boolean[] validID = new boolean[maxId + 1];
        for (User user : allUsers) {
            validID[user.getId().intValue()] = true;
        }

        // TODO: I should update the graph instead of always building it
        Graph graph = new Graph(maxId + 1);

        Iterable<Friendship> allFriendships = this.friendshipRepository.findAll();

        for (Friendship friendship : allFriendships) {
            int userID1 = friendship.getId().getFirst().intValue();
            int userID2 = friendship.getId().getSecond().intValue();
            graph.addEdge(userID1, userID2);
        }

        return graph.connectedComponents(validID);
    }

    public List<Integer> getMostSocialCommunity() {
        // TODO: blue
        return new ArrayList<>();
    }
}
