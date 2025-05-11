package com.github.ideepakkalra.eventmanagement.controller;

import com.github.ideepakkalra.eventmanagement.entity.CommunityReferral;
import com.github.ideepakkalra.eventmanagement.entity.Login;
import com.github.ideepakkalra.eventmanagement.entity.User;
import com.github.ideepakkalra.eventmanagement.model.BaseResponse;
import com.github.ideepakkalra.eventmanagement.model.UserRequest;
import com.github.ideepakkalra.eventmanagement.model.UserResponse;
import com.github.ideepakkalra.eventmanagement.services.CommunityReferralService;
import com.github.ideepakkalra.eventmanagement.services.LoginService;
import com.github.ideepakkalra.eventmanagement.services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommunityReferralService communityReferralService;

    @Autowired
    private LoginService loginService;

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
            if (communityReferral != null) {
                if (!communityReferral.getPhoneNumber().equals(userRequest.getPhoneNumber())) {
                    userResponse.setStatus(BaseResponse.Status.FAILURE);
                    userResponse.setMessage("Invalid phone number in referral.");
                    return ResponseEntity.internalServerError().body(userResponse);
                }
                User user = userRequestToUserModelMapper.map(userRequest, User.class);
                user.setReferredBy(communityReferral.getReferrer());
                user = userService.create(user);
                Login login = userRequestToLoginModelMapper.map(userRequest, Login.class);
                login.setUser(user);
                login = loginService.create(login);
                userToUserResponseMapper.map(user, userResponse);
                loginService.login(login);
                userResponse.setStatus(BaseResponse.Status.SUCCESS);
                return ResponseEntity.ok(userResponse);
            } else {
                userResponse.setStatus(BaseResponse.Status.FAILURE);
                userResponse.setMessage("Invalid referral id / code.");
                return ResponseEntity.badRequest().body(userResponse);
            }
        } catch (Exception e) {
            userResponse.setStatus(BaseResponse.Status.FAILURE);
            userResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(userResponse);
        }
    }
}
