package com.github.ideepakkalra.eventmanagement.db;

import com.github.ideepakkalra.eventmanagement.entity.CommunityReferral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityReferralRepository extends JpaRepository<CommunityReferral, Long> {

    @Query ("SELECT cr FROM CommunityReferral cr WHERE cr.id = :referralId AND cr.code = :referralCode")
    CommunityReferral findByIdAndCode(@Param("referralId") Long referralId, @Param("referralCode") String referralCode);
}
