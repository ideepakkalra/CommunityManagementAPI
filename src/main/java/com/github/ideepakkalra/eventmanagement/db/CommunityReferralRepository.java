package com.github.ideepakkalra.eventmanagement.db;

import com.github.ideepakkalra.eventmanagement.entity.CommunityReferral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityReferralRepository extends JpaRepository<CommunityReferral, Long> {
}
