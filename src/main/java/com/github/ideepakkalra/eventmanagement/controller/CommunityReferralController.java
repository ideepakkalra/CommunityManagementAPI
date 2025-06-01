package com.github.ideepakkalra.eventmanagement.controller;

import com.github.ideepakkalra.eventmanagement.entity.CommunityReferral;
import com.github.ideepakkalra.eventmanagement.entity.User;
import com.github.ideepakkalra.eventmanagement.exceptions.UserNotFoundException;
import com.github.ideepakkalra.eventmanagement.model.CommunityReferralRequest;
import com.github.ideepakkalra.eventmanagement.model.CommunityReferralResponse;
import com.github.ideepakkalra.eventmanagement.services.CommunityReferralService;
import com.github.ideepakkalra.eventmanagement.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @GetMapping (value = "/referral/{referralId}/{referralCode}")
    public ResponseEntity<CommunityReferralResponse> getByReferralIdAndCode(@PathVariable Long referralId, @PathVariable @Pattern(regexp = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}",
            message = "Invalid code.") String referralCode) {
        CommunityReferralResponse communityReferralResponse = new CommunityReferralResponse();
        CommunityReferral communityReferral = communityReferralService.selectByIdAndCode(referralId, referralCode);
        if (communityReferral != null) {
            communicationReferralToCommunicationReferralResponseModelMapper.map(communityReferral, communityReferralResponse);
            return ResponseEntity.ok(communityReferralResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping (value = "/referral/referrer/id/{referrerId}")
    public ResponseEntity<List<CommunityReferralResponse>> getByReferrer(@PathVariable Long referrerId) {
        List<CommunityReferralResponse> communityReferralResponses = new ArrayList<>();
        List<CommunityReferral> communityReferrals = communityReferralService.selectByReferrerId(referrerId);
        communityReferrals.forEach((communityReferral) -> communityReferralResponses.add(communicationReferralToCommunicationReferralResponseModelMapper.map(communityReferral, CommunityReferralResponse.class)));
        return ResponseEntity.ok(communityReferralResponses);
    }

    @PostMapping (value = "/referral")
    public ResponseEntity<CommunityReferralResponse> post(@Valid @RequestBody CommunityReferralRequest communityReferralRequest, HttpSession httpSession) {
        CommunityReferralResponse communityReferralResponse = new CommunityReferralResponse();
        if (communityReferralRequest.getId() != null || communityReferralRequest.getCode() != null || communityReferralRequest.getVersion() != null || !"OPEN".equals(communityReferralRequest.getState())) {
            return ResponseEntity.badRequest().body(communityReferralResponse);
        }
        try {
            CommunityReferral communityReferral = communicationReferralRequestToCommunicationReferralModelMapper.map(communityReferralRequest, CommunityReferral.class);
            User user = userService.selectById((Long) httpSession.getAttribute("user.id"));
            communityReferral.setReferrer(user);
            communityReferral.setUpdatedBy(user);
            communityReferral.setUpdatedOn(new Date());
            communityReferral = communityReferralService.create(communityReferral);
            communicationReferralToCommunicationReferralResponseModelMapper.map(communityReferral, communityReferralResponse);
            return ResponseEntity.ok(communityReferralResponse);
        } catch (UserNotFoundException unfe) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping (value = "/referral")
    public ResponseEntity<CommunityReferralResponse> put(@Valid @RequestBody CommunityReferralRequest communityReferralRequest, HttpSession httpSession) {
        CommunityReferralResponse communityReferralResponse = new CommunityReferralResponse();
        if (communityReferralRequest.getId() == null || communityReferralRequest.getCode() == null || communityReferralRequest.getVersion() == null || communityReferralRequest.getState() == null || communityReferralRequest.getReferrer() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!"ADMIN".equals(httpSession.getAttribute("user.type"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(communityReferralResponse);
        }
        try {
            CommunityReferral communityReferral = communicationReferralRequestToCommunicationReferralModelMapper.map(communityReferralRequest, CommunityReferral.class);
            User referrerUser = userService.selectById(communityReferralRequest.getReferrer());
            communityReferral.setReferrer(referrerUser);
            User updatedByUser = userService.selectById((Long) httpSession.getAttribute("user.id"));
            communityReferral.setUpdatedBy(updatedByUser);
            communityReferral.setUpdatedOn(new Date());
            communityReferral = communityReferralService.update(communityReferral);
            communicationReferralToCommunicationReferralResponseModelMapper.map(communityReferral, communityReferralResponse);
            return ResponseEntity.ok(communityReferralResponse);
        } catch (UserNotFoundException unfe) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
