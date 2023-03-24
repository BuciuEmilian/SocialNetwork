package com.example.socialnetwork.repository.file;

import com.example.socialnetwork.domain.User;

import java.util.List;

public class FileUserRepository extends AbstractFileRepository<Long, User> {

    public FileUserRepository(String fileName) {
        super(fileName);
    }

    @Override
    public User stringToEntity(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2), "", "");
        user.setId(Long.parseLong(attributes.get(0)));

        return user;
    }

    @Override
    public String entityToString(User user) {
        return user.getId() + ";" + user.getFirstName() + ";" + user.getLastName();
    }
}
