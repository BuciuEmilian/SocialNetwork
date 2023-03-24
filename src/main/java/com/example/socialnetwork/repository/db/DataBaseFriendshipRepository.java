package com.example.socialnetwork.repository.db;

import com.example.socialnetwork.domain.Pair;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.utils.Constants;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class DataBaseFriendshipRepository implements Repository<Pair<Long>, Friendship> {
    private final String url;
    private final String usernameDB;
    private final String passwordDB;

    public DataBaseFriendshipRepository(String url, String usernameDB, String passwordDB) {
        this.url = url;
        this.usernameDB = usernameDB;
        this.passwordDB = passwordDB;
    }

    @Override
    public Friendship findOne(Pair<Long> id) {
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships WHERE id1 = ? and id2 = ? or id1 = ? and id2 = ?")) {

            statement.setLong(1, id.getFirst());
            statement.setLong(2, id.getSecond());
            statement.setLong(3, id.getSecond());
            statement.setLong(4, id.getFirst());
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next())
                return null;

            LocalDate date = resultSet.getDate(3).toLocalDate();
            LocalTime time = resultSet.getTime(4).toLocalTime();
            LocalDateTime friendsFrom = date.atTime(time);

            Friendship friendship = new Friendship(friendsFrom);
            friendship.setId(id);

            return friendship;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                LocalDate date = LocalDate.parse(resultSet.getString("date_friends_from"), Constants.DATE_FORMATTER);
                LocalTime time = LocalTime.parse(resultSet.getString("time_friends_from"), Constants.TIME_FORMATTER);
                LocalDateTime friendsFrom = LocalDateTime.of(date, time);
                String status = resultSet.getString(5);

                Friendship friendship = new Friendship(friendsFrom);
                friendship.setId(new Pair<>(id1, id2));
                friendship.setStatus(status);

                friendships.add(friendship);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship friendship) {
        if (friendship == null)
            throw new IllegalArgumentException("Friendship must not be null");

        // first I need to account for the fact that 'insert into' can't verify the equality of the ids correctly,
        // so I will search for all the friendships that might have the userIds in another order
        Friendship m_friendship = this.findOne(friendship.getId());
        if (m_friendship != null)
            return m_friendship;

        String sql = "INSERT INTO friendships (id1, id2, date_friends_from, time_friends_from, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, friendship.getId().getFirst());
            statement.setLong(2, friendship.getId().getSecond());
            statement.setDate(3, java.sql.Date.valueOf(friendship.getFriendsFrom().toLocalDate()));
            statement.setTime(4, java.sql.Time.valueOf(friendship.getFriendsFrom().toLocalTime()));
            statement.setString(5, friendship.getStatus());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public Friendship delete(Pair<Long> id) {
        if (id == null)
            throw new IllegalArgumentException("Friendship must not be null");

        String sql = "DELETE FROM friendships WHERE id1 = ? AND id2 = ? OR id1 = ? AND id2 = ?";
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            Friendship friendship = this.findOne(id);

            statement.setLong(1, id.getFirst());
            statement.setLong(2, id.getSecond());
            statement.setLong(3, id.getSecond());
            statement.setLong(4, id.getFirst());

            if (statement.executeUpdate() > 0)
                return friendship;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Friendship update(Friendship friendship) {
        if (friendship == null)
            throw new IllegalArgumentException("User must not be null!");

        String sql = "UPDATE friendships SET date_friends_from = ?, time_friends_from = ?, status = ? WHERE id1 = ? AND id2 = ?";
        try (Connection connection = DriverManager.getConnection(url, usernameDB, passwordDB);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, java.sql.Date.valueOf(friendship.getFriendsFrom().toLocalDate()));
            statement.setTime(2, java.sql.Time.valueOf(friendship.getFriendsFrom().toLocalTime()));
            statement.setString(3, friendship.getStatus());
            statement.setLong(4, friendship.getId().getFirst());
            statement.setLong(5, friendship.getId().getSecond());


            if (statement.executeUpdate() > 0) {
                return friendship ;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
