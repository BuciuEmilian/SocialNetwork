package com.example.socialnetwork.repository.db;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.domain.Pair;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataBaseMessagesRepository implements Repository<Long, Message> {
    private final String url;
    private final String usernameDB;
    private final String passwordDB;

    public DataBaseMessagesRepository(String url, String usernameDB, String passwordDB) {
        this.url = url;
        this.usernameDB = usernameDB;
        this.passwordDB = passwordDB;
    }

    @Override
    public Message findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE id = ?")) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next())
                return null;

            Message message = new Message(
                    resultSet.getLong("id1"),
                    resultSet.getLong("id2"),
                    resultSet.getString("body"),
                    resultSet.getTimestamp("date_time").toLocalDateTime()
            );
            message.setId(id);

            return message;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages ORDER BY date_time");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                String body = resultSet.getString("body");
                Timestamp time_stamp = resultSet.getTimestamp("date_time");
                LocalDateTime date_time = time_stamp.toLocalDateTime();
                Long id = resultSet.getLong("id");

                Message message = new Message(id1, id2, body, date_time);
                message.setId(id);

                messages.add(message);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    @Override
    public Message save(Message message) {
        if (message == null)
            throw new IllegalArgumentException("Message must not be null");

        String sql = "INSERT INTO messages (id1, id2, body, date_time) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, message.getFrom());
            statement.setLong(2, message.getTo());
            statement.setString(3, message.getBody());
            statement.setTimestamp(4, Timestamp.valueOf(message.getDateTime()));

            if (statement.executeUpdate() > 0) {
                return message;
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public Message delete(Long id) {
        if (id == null)
            throw new IllegalArgumentException("ID must not be null!");

        String sql = "DELETE FROM messages WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            Message message = this.findOne(id);

            statement.setLong(1, id);

            if (statement.executeUpdate() > 0)
                return message;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Message update(Message message) {
        return null;
    }
}
