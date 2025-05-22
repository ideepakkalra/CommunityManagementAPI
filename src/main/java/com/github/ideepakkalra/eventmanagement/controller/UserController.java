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
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<UserResponse> post(@Valid @RequestBody UserRequest userRequest, HttpSession httpSession) {
        UserResponse userResponse = new UserResponse();
        try {
            CommunityReferral communityReferral = communityReferralService.selectByIdAndCode(userRequest.getReferralId(), userRequest.getReferralCode());
            if (communityReferral != null && "OPEN".equals(communityReferral.getState().toString()) && userRequest.getId() == null) {
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
                communityReferral.setState(CommunityReferral.State.CLOSED);
                communityReferralService.update(communityReferral);
                httpSession.setAttribute("user.id", login.getUser().getId());
                httpSession.setAttribute("user.type", login.getUser().getType());
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

    @PutMapping (value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> put(@Valid @RequestBody UserRequest userRequest, HttpSession httpSession) {
        UserResponse userResponse = new UserResponse();
        try {
            User user = userService.selectById(userRequest.getId());
            // Common validations
            if (userRequest.getId() < 0 || userRequest.getVersion() < 0 || !user.getPhoneNumber().equals(userRequest.getPhoneNumber())) {
                userResponse.setStatus(BaseResponse.Status.FAILURE);
                userResponse.setMessage("Invalid user data.");
                return ResponseEntity.badRequest().body(userResponse);
            }
            // Standard user validations
            if ("STANDARD".equals(httpSession.getAttribute("user.type"))) {
                // Can only update own record
                if (!httpSession.getAttribute("user.id").equals(userRequest.getId())) {
                    userResponse.setStatus(BaseResponse.Status.FAILURE);
                    userResponse.setMessage("Invalid request");
                    return ResponseEntity.badRequest().body(userResponse);
                }
            } else {
                // Can not update own record
                if (httpSession.getAttribute("user.id").equals(userRequest.getId())) {
                    userResponse.setStatus(BaseResponse.Status.FAILURE);
                    userResponse.setMessage("Invalid request");
                    return ResponseEntity.badRequest().body(userResponse);
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
            user.setUpdatedBy(userService.selectById(Long.valueOf(httpSession.getAttribute("user.id").toString())));
            user = userService.update(user);
            userToUserResponseMapper.map(user, userResponse);
            userResponse.setStatus(BaseResponse.Status.SUCCESS);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            userResponse.setStatus(BaseResponse.Status.FAILURE);
            userResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(userResponse);
        }
    }
}
