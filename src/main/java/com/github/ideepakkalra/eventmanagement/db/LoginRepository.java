package com.github.ideepakkalra.eventmanagement.db;

import com.github.ideepakkalra.eventmanagement.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {

    @Query("SELECT l FROM Login l WHERE l.phoneNumber = :phoneNumber")
    Login findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query ("SELECT l FROM Login l WHERE l.phoneNumber = :phoneNumber AND passcode = :passcode")
    Login findByPhoneNumberAndPasscode(@Param("phoneNumber") String phoneNumber, @Param("passcode") String passcode);
}
