package com.github.ideepakkalra.eventmanagement.services;

import com.github.ideepakkalra.eventmanagement.db.UserRepository;
import com.github.ideepakkalra.eventmanagement.entity.User;
import com.github.ideepakkalra.eventmanagement.exceptions.UserNotFoundException;
import com.github.ideepakkalra.eventmanagement.model.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(UserRequest userRequest) {
        // create user, login both objects here if new, otherwise just user obj
        //return userRepository.save(null);
        return null;
    }

    public User getUserById(Long id) throws Exception {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
}
