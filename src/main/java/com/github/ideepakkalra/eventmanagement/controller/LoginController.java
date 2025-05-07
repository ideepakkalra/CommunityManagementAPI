package com.github.ideepakkalra.eventmanagement.controller;

import com.github.ideepakkalra.eventmanagement.entity.User;
import com.github.ideepakkalra.eventmanagement.exceptions.AccountNotFoundException;
import com.github.ideepakkalra.eventmanagement.exceptions.InvalidCredentialsException;
import com.github.ideepakkalra.eventmanagement.model.LoginRequest;
import com.github.ideepakkalra.eventmanagement.model.LoginResponse;
import com.github.ideepakkalra.eventmanagement.services.LoginService;
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
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping (value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpSession httpSession) {
        try {
            User user = loginService.login(loginRequest);
            httpSession.setAttribute("user.type", user.getType());
            return ResponseEntity.ok(new LoginResponse(LoginResponse.Status.SUCCESS, "Login successful [" + user.getId() + "]"));
        } catch (InvalidCredentialsException ice) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse(LoginResponse.Status.FAILURE, ice.getMessage()));
        } catch (AccountNotFoundException nfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(LoginResponse.Status.FAILURE, nfe.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new LoginResponse(LoginResponse.Status.FAILURE, e.getMessage()));
        }
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> logout(@Valid @RequestBody LoginRequest loginRequest, HttpSession httpSession) {
        try {
            User user = loginService.logout(loginRequest);
            httpSession.invalidate();
            return ResponseEntity.ok(new LoginResponse(LoginResponse.Status.SUCCESS, "Logout successful [" + user.getId() + "]"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new LoginResponse(LoginResponse.Status.FAILURE, e.getMessage()));
        }
    }
}
