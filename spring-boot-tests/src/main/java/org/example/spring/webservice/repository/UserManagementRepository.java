package org.example.spring.webservice.repository;

import org.example.spring.webservice.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserManagementRepository extends JpaRepository<UserAccount, Long> {}
