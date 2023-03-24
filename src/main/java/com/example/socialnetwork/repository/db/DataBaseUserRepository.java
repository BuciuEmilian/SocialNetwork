package com.example.socialnetwork.repository.db;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DataBaseUserRepository implements Repository<Long, User> {
    private final String url;
    private final String usernameDB;
    private final String passwordDB;

    public DataBaseUserRepository(String url, String usernameDB, String passwordDB) {
        this.url = url;
        this.usernameDB = usernameDB;
        this.passwordDB = passwordDB;
    }

    @Override
    public User findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next())
                return null;

            User user = new User(
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
            );
            user.setId(id);

            return user;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                User user = new User(first_name, last_name, username, password);
                user.setId(id);

                users.add(user);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }

    @Override
    public User save(User user) {
        if (user == null)
            throw new IllegalArgumentException("User must not be null");

        String sql = "INSERT INTO users (first_name, last_name, username, password) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getPassword());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public User delete(Long id) {
        if (id == null)
            throw new IllegalArgumentException("ID must not be null!");

        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            User user = this.findOne(id);

            statement.setLong(1, id);

            if (statement.executeUpdate() > 0)
                return user;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public User update(User user) {
        if (user == null)
            throw new IllegalArgumentException("User must not be null!");

        String sql = "UPDATE users SET first_name = ?, last_name = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement(sql)) {


            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setLong(3, user.getId());

            if (statement.executeUpdate() > 0) {
                return user;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
