package org.example.spring.r2dbc.tests;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Interface definition that declares:
 * - The name for our Spring Data repository
 * - Spring Data Commons’ base interface for any reactive repository, ReactiveCrudRepository as a parent interface
 * - The domain and primary key’s type for this repository
 */
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {}
