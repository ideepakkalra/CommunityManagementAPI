package com.github.ideepakkalra.eventmanagement.controller;

import com.github.ideepakkalra.eventmanagement.model.LoginRequest;
import com.github.ideepakkalra.eventmanagement.model.LoginResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpSession httpSession) {
        return null;
    }
}
