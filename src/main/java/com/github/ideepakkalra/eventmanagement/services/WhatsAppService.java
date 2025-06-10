package com.github.ideepakkalra.eventmanagement.services;

import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    public Boolean sendCommunityReferralInvite(String phoneNumber, String code) {
        System.out.println(phoneNumber + "\t" + code);
        return true;
    }
}
