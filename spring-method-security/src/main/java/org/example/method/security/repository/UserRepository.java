package org.example.method.security.repository;

import org.example.method.security.model.UserAccount;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<UserAccount, Long> {
	UserAccount findByUsername(String username);
}
