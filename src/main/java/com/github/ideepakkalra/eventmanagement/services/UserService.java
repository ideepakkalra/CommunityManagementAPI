package com.github.ideepakkalra.eventmanagement.services;

import com.github.ideepakkalra.eventmanagement.db.UserRepository;
import com.github.ideepakkalra.eventmanagement.entity.User;
import com.github.ideepakkalra.eventmanagement.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User create(User user) {
        user.setUpdatedOn(new Date());
        return userRepository.save(user);
    }

    public User selectById(Long id) throws Exception {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User selectByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
}
