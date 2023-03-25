package org.example.spring.webservice.repository;

import org.example.spring.webservice.model.UserAccount;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<UserAccount, Long> {
	UserAccount findByUsername(String username);
}
