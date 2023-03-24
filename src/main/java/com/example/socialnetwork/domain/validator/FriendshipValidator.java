package com.example.socialnetwork.domain.validator;

import com.example.socialnetwork.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errorMessage = "";
        if (entity.getId().getFirst() == null)
            errorMessage += "First ID is null!\n";
        if (entity.getId().getSecond() == null)
            errorMessage += "Second ID is null!\n";
        if (!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }
}
