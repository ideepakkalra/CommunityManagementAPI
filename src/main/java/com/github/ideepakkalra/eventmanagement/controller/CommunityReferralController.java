package com.github.ideepakkalra.eventmanagement.controller;

import com.github.ideepakkalra.eventmanagement.entity.CommunityReferral;
import com.github.ideepakkalra.eventmanagement.entity.User;
import com.github.ideepakkalra.eventmanagement.exceptions.UserNotFoundException;
import com.github.ideepakkalra.eventmanagement.model.BaseResponse;
import com.github.ideepakkalra.eventmanagement.model.CommunityReferralRequest;
import com.github.ideepakkalra.eventmanagement.model.CommunityReferralResponse;
import com.github.ideepakkalra.eventmanagement.services.CommunityReferralService;
import com.github.ideepakkalra.eventmanagement.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class CommunityReferralController {

    @Autowired
    private CommunityReferralService communityReferralService;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier (value = "communicationReferralRequestToCommunicationReferralModelMapper")
    private ModelMapper communicationReferralRequestToCommunicationReferralModelMapper;

    @Autowired
    @Qualifier (value = "communicationReferralToCommunicationReferralResponseModelMapper")
    private ModelMapper communicationReferralToCommunicationReferralResponseModelMapper;

    @PostMapping (value = "/referral")
    public ResponseEntity<CommunityReferralResponse> post(@Valid @RequestBody CommunityReferralRequest communityReferralRequest, HttpSession httpSession) {
        CommunityReferralResponse communityReferralResponse = new CommunityReferralResponse();
        if (communityReferralRequest.getId() != null || communityReferralRequest.getCode() != null || communityReferralRequest.getVersion() != null || !"OPEN".equals(communityReferralRequest.getState())) {
            communityReferralResponse.setStatus(BaseResponse.Status.FAILURE);
            communityReferralResponse.setMessage("Invalid id / code / version / state.");
            return ResponseEntity.badRequest().body(communityReferralResponse);
        }
        System.err.println(communityReferralRequest);
        try {
            CommunityReferral communityReferral = communicationReferralRequestToCommunicationReferralModelMapper.map(communityReferralRequest, CommunityReferral.class);
            User user = userService.getUserById((Long) httpSession.getAttribute("user.id"));
            communityReferral.setReferrer(user);
            communityReferral.setUpdatedBy(user);
            communityReferral.setUpdatedOn(new Date());
            communityReferral = communityReferralService.create(communityReferral);
            communicationReferralToCommunicationReferralResponseModelMapper.map(communityReferral, communityReferralResponse);
            communityReferralResponse.setStatus(BaseResponse.Status.SUCCESS);
            return ResponseEntity.ok(communityReferralResponse);
        } catch (UserNotFoundException unfe) {
            communityReferralResponse.setStatus(BaseResponse.Status.FAILURE);
            communityReferralResponse.setMessage(unfe.getMessage());
            return ResponseEntity.badRequest().body(communityReferralResponse);
        } catch (Exception e) {
            e.printStackTrace();
            communityReferralResponse.setStatus(BaseResponse.Status.FAILURE);
            communityReferralResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(communityReferralResponse);
        }
    }

    @PutMapping (value = "/referral")
    public ResponseEntity<CommunityReferralResponse> put(@Valid @RequestBody CommunityReferralRequest communityReferralRequest, HttpSession httpSession) {
        CommunityReferralResponse communityReferralResponse = new CommunityReferralResponse();
        if (communityReferralRequest.getId() == null || communityReferralRequest.getCode() == null || communityReferralRequest.getVersion() == null || communityReferralRequest.getState() == null || communityReferralRequest.getReferrer() == null) {
            communityReferralResponse.setStatus(BaseResponse.Status.FAILURE);
            communityReferralResponse.setMessage("Invalid id / code / version / referrer / state.");
            return ResponseEntity.badRequest().body(communityReferralResponse);
        }
        if (!"ADMIN".equals(httpSession.getAttribute("user.type"))) {
            communityReferralResponse.setStatus(BaseResponse.Status.FAILURE);
            communityReferralResponse.setMessage("Insufficient privilege.");
            return ResponseEntity.badRequest().body(communityReferralResponse);
        }
        try {
            CommunityReferral communityReferral = communicationReferralRequestToCommunicationReferralModelMapper.map(communityReferralRequest, CommunityReferral.class);
            User referrerUser = userService.getUserById(communityReferralRequest.getReferrer());
            communityReferral.setReferrer(referrerUser);
            User updatedByUser = userService.getUserById((Long) httpSession.getAttribute("user.id"));
            communityReferral.setUpdatedBy(updatedByUser);
            communityReferral.setUpdatedOn(new Date());
            communityReferral = communityReferralService.update(communityReferral);
            communicationReferralToCommunicationReferralResponseModelMapper.map(communityReferral, communityReferralResponse);
            communityReferralResponse.setStatus(BaseResponse.Status.SUCCESS);
            return ResponseEntity.ok(communityReferralResponse);
        } catch (UserNotFoundException unfe) {
            communityReferralResponse.setStatus(BaseResponse.Status.FAILURE);
            communityReferralResponse.setMessage(unfe.getMessage());
            return ResponseEntity.badRequest().body(communityReferralResponse);
        } catch (Exception e) {
            e.printStackTrace();
            communityReferralResponse.setStatus(BaseResponse.Status.FAILURE);
            communityReferralResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(communityReferralResponse);
        }
    }
}
