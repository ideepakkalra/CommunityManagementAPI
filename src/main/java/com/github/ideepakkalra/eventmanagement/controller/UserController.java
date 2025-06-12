package com.github.ideepakkalra.eventmanagement.controller;

import com.github.ideepakkalra.eventmanagement.entity.CommunityReferral;
import com.github.ideepakkalra.eventmanagement.entity.Login;
import com.github.ideepakkalra.eventmanagement.entity.User;
import com.github.ideepakkalra.eventmanagement.model.UserRequest;
import com.github.ideepakkalra.eventmanagement.model.UserResponse;
import com.github.ideepakkalra.eventmanagement.services.CommunityReferralService;
import com.github.ideepakkalra.eventmanagement.services.JWTService;
import com.github.ideepakkalra.eventmanagement.services.LoginService;
import com.github.ideepakkalra.eventmanagement.services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommunityReferralService communityReferralService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    @Qualifier(value = "userRequestToLoginModelMapper")
    private ModelMapper userRequestToLoginModelMapper;

    @Autowired
    @Qualifier(value = "userRequestToUserModelMapper")
    private ModelMapper userRequestToUserModelMapper;

    @Autowired
    @Qualifier(value = "userToUserResponseMapper")
    private ModelMapper userToUserResponseMapper;

    @PostMapping (value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> post(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = new UserResponse();
        try {
            CommunityReferral communityReferral = communityReferralService.selectByIdAndCode(userRequest.getReferralId(), userRequest.getReferralCode());
            if (communityReferral != null && "OPEN".equals(communityReferral.getState().toString()) && userRequest.getId() == null) {
                if (!communityReferral.getPhoneNumber().equals(userRequest.getPhoneNumber())) {
                    return ResponseEntity.internalServerError().build();
                }
                User user = userRequestToUserModelMapper.map(userRequest, User.class);
                user.setReferredBy(communityReferral.getReferrer());
                user = userService.create(user);
                Login login = userRequestToLoginModelMapper.map(userRequest, Login.class);
                login.setUser(user);
                login = loginService.create(login);
                userToUserResponseMapper.map(user, userResponse);
                loginService.login(login);
                communityReferral.setState(CommunityReferral.State.CLOSED);
                communityReferralService.update(communityReferral);
                return ResponseEntity.ok(userResponse);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping (value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> put(@Valid @RequestBody UserRequest userRequest, @RequestHeader("Authorization") String authorization) {
        UserResponse userResponse = new UserResponse();
        try {
            Long sub = jwtService.getSub(authorization);
            String role = jwtService.getRole(authorization);
            User user = userService.selectById(userRequest.getId());
            // Common validations
            if (userRequest.getId() < 0 || userRequest.getVersion() < 0 || !user.getPhoneNumber().equals(userRequest.getPhoneNumber())) {
                return ResponseEntity.badRequest().build();
            }
            // Standard user validations
            if ("STANDARD".equals(role)) {
                // Can only update own record
                if (!userRequest.getId().equals(sub)) {
                    return ResponseEntity.badRequest().build();
                }
            } else {
                // Can not update own record
                if (userRequest.getId().equals(sub)) {
                    return ResponseEntity.badRequest().build();
                }
            }
            if (userRequest.getPasscode() != null) {
                Login login = loginService.selectByPhoneNumber(userRequest.getPhoneNumber());
                login.setPasscode(userRequest.getPasscode());
                login.setRetryCount(0);
                login.setState(Login.State.SUCCESS);
                loginService.update(login);
            }
            userRequestToUserModelMapper.map(userRequest, user);
            user.setReferredBy(userService.selectById(userRequest.getId()));
            user.setUpdatedBy(userService.selectById(sub));
            user = userService.update(user);
            userToUserResponseMapper.map(user, userResponse);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
