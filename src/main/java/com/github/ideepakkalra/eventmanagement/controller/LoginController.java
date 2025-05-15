package com.github.ideepakkalra.eventmanagement.controller;

import com.github.ideepakkalra.eventmanagement.entity.Login;
import com.github.ideepakkalra.eventmanagement.exceptions.AccountNotFoundException;
import com.github.ideepakkalra.eventmanagement.exceptions.InvalidCredentialsException;
import com.github.ideepakkalra.eventmanagement.model.BaseResponse;
import com.github.ideepakkalra.eventmanagement.model.LoginRequest;
import com.github.ideepakkalra.eventmanagement.model.LoginResponse;
import com.github.ideepakkalra.eventmanagement.services.LoginService;
import jakarta.servlet.http.HttpSession;
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
    @Qualifier(value = "loginRequestToLoginModelMapper")
    private ModelMapper loginRequestToLoginModelMapper;

    @Autowired
    @Qualifier (value = "loginToLoginResponseModelMapper")
    private ModelMapper loginToLoginResponseModelMapper;

    @PostMapping (value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpSession httpSession) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatus(BaseResponse.Status.FAILURE);
        try {
            Login login = loginService.login(loginRequestToLoginModelMapper.map(loginRequest, Login.class));
            httpSession.setAttribute("user.id", login.getUser().getId());
            httpSession.setAttribute("user.type", login.getUser().getType());
            loginToLoginResponseModelMapper.map(login, loginResponse);
            loginResponse.setStatus(LoginResponse.Status.SUCCESS);
            return ResponseEntity.ok(loginResponse);
        } catch (InvalidCredentialsException ice) {
            loginResponse.setMessage(ice.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginResponse);
        } catch (AccountNotFoundException nfe) {
            loginResponse.setMessage(nfe.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(loginResponse);
        } catch (Exception e) {
            loginResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(loginResponse);
        }
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> logout(@Valid @RequestBody LoginRequest loginRequest, HttpSession httpSession) {
        LoginResponse loginResponse = new LoginResponse();

        try {
            Login login = loginService.logout(loginRequestToLoginModelMapper.map(loginRequest, Login.class));
            httpSession.invalidate();
            loginToLoginResponseModelMapper.map(login, loginResponse);
            loginResponse.setStatus(BaseResponse.Status.SUCCESS);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            loginResponse.setStatus(BaseResponse.Status.FAILURE);
            loginResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(loginResponse);
        }
    }
}
