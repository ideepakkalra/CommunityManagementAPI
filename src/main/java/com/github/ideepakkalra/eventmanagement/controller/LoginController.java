package com.github.ideepakkalra.eventmanagement.controller;

import com.github.ideepakkalra.eventmanagement.entity.Login;
import com.github.ideepakkalra.eventmanagement.exceptions.AccountNotFoundException;
import com.github.ideepakkalra.eventmanagement.exceptions.InvalidCredentialsException;
import com.github.ideepakkalra.eventmanagement.model.LoginRequest;
import com.github.ideepakkalra.eventmanagement.model.LoginResponse;
import com.github.ideepakkalra.eventmanagement.model.UserResponse;
import com.github.ideepakkalra.eventmanagement.services.JWTService;
import com.github.ideepakkalra.eventmanagement.services.LoginService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    private JWTService jwtService;

    @Autowired
    @Qualifier(value = "loginRequestToLoginModelMapper")
    private ModelMapper loginRequestToLoginModelMapper;

    @Autowired
    @Qualifier (value = "loginToLoginResponseModelMapper")
    private ModelMapper loginToLoginResponseModelMapper;

    @Autowired
    @Qualifier (value = "userToUserResponseMapper")
    private ModelMapper userToUserResponseMapper;

    @PostMapping (value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        try {
            Login login = loginService.login(loginRequestToLoginModelMapper.map(loginRequest, Login.class));
            loginToLoginResponseModelMapper.map(login, loginResponse);
            loginResponse.setToken(jwtService.generateToken(String.valueOf(login.getUser().getId()), String.valueOf(login.getUser().getType())));
            loginResponse.setUser(userToUserResponseMapper.map(login.getUser(), UserResponse.class));
            return ResponseEntity.ok(loginResponse);
        } catch (InvalidCredentialsException ice) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (AccountNotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> logout(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        try {
            Login login = loginService.logout(loginRequestToLoginModelMapper.map(loginRequest, Login.class));
            loginToLoginResponseModelMapper.map(login, loginResponse);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
