package com.github.ideepakkalra.eventmanagement.services;

import com.github.ideepakkalra.eventmanagement.db.CommunityReferralRepository;
import com.github.ideepakkalra.eventmanagement.entity.CommunityReferral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommunityReferralService {

    @Autowired
    private CommunityReferralRepository communityReferralRepository;

    public CommunityReferral create(CommunityReferral communityReferral) {
        communityReferral.setCode(UUID.randomUUID().toString());
        return communityReferralRepository.save(communityReferral);
    }

    public CommunityReferral update(CommunityReferral communityReferral) {
        return communityReferralRepository.save(communityReferral);
    }

    public CommunityReferral selectByIdAndCode(Long referralId, String referralCode) {
        return communityReferralRepository.findByIdAndCode(referralId, referralCode);
    }

    public List<CommunityReferral> selectByReferrerId(Long referrerId) {
        return communityReferralRepository.findByReferrerId(referrerId);
    }

    public CommunityReferral selectById(Long referrerId) {
        return communityReferralRepository.findById(referrerId).orElse(null);
    }
}
