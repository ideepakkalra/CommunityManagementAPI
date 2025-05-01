package com.github.ideepakkalra.eventmanagement.controller;

import com.github.ideepakkalra.eventmanagement.entity.User;
import com.github.ideepakkalra.eventmanagement.model.LoginRequest;
import com.github.ideepakkalra.eventmanagement.model.LoginResponse;
import com.github.ideepakkalra.eventmanagement.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpSession httpSession) {
        LoginResponse loginResponse = new LoginResponse();
        User user = userService.getUserByPhoneNumber(loginRequest.getPhoneNumber());
        if (user == null) {
            loginResponse.setStatus(LoginResponse.Status.FAILURE);
            loginResponse.setMessage("User not found with phone number [" + loginRequest + "].");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(loginResponse);
        } else {
            loginResponse.setStatus(LoginResponse.Status.SUCCESS);
            loginResponse.setMessage("User found with phone number [" + loginRequest + "].");
            return ResponseEntity.ok(loginResponse);
        }
        // TODO: Return unauthorized in error
    }
}
