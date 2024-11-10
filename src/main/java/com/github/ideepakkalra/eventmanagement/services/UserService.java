package com.github.ideepakkalra.eventmanagement.services;

import com.github.ideepakkalra.eventmanagement.db.UserRepository;
import com.github.ideepakkalra.eventmanagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
