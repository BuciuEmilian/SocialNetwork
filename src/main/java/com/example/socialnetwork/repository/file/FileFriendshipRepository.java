package com.example.socialnetwork.repository.file;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.Pair;

import java.time.LocalDateTime;
import java.util.List;

public class FileFriendshipRepository extends AbstractFileRepository<Pair<Long>, Friendship> {

    public FileFriendshipRepository(String fileName) {
        super(fileName);
    }

    @Override
    public Friendship stringToEntity(List<String> attributes) {
        Friendship friendship = new Friendship(LocalDateTime.parse(attributes.get(2)));
        friendship.setId(new Pair<>(Long.parseLong(attributes.get(0)), Long.parseLong(attributes.get(1))));

        return friendship;
    }

    @Override
    public String entityToString(Friendship friendship) {
        return friendship.getId().getFirst() + ";" + friendship.getId().getSecond() + ";" + friendship.getFriendsFrom();
    }
}
