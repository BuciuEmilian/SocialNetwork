package com.example.socialnetwork.domain.validator;

import com.example.socialnetwork.domain.User;

public class UserValidator implements Validator<User>{
    @Override
    public void validate(User entity) throws ValidationException {
        String errorMessage = "";
        if (entity.getFirstName() == null || entity.getFirstName().isEmpty())
            errorMessage += "First name can't be empty. \n";
        if (entity.getLastName() == null || entity.getLastName().isEmpty())
            errorMessage +="Last name can't be empty. \n";

        // TODO: username and password restrictions (maybe for name too)

        if (!errorMessage.equals(""))
            throw new ValidationException(errorMessage);
    }
}
