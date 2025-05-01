package com.github.ideepakkalra.eventmanagement.db;

import com.github.ideepakkalra.eventmanagement.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {
}
