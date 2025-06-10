package com.github.ideepakkalra.eventmanagement.db;

import com.github.ideepakkalra.eventmanagement.entity.CommunityReferral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityReferralRepository extends JpaRepository<CommunityReferral, Long> {

    @Query ("SELECT cr FROM CommunityReferral cr WHERE cr.id = :referralId AND cr.code = :referralCode")
    CommunityReferral findByIdAndCode(@Param("referralId") Long referralId, @Param("referralCode") String referralCode);

    @Query ("SELECT cr FROM CommunityReferral cr WHERE cr.referrer.id = :referrerId")
    List<CommunityReferral> findByReferrerId(@Param("referrerId") Long referrerId);
}
