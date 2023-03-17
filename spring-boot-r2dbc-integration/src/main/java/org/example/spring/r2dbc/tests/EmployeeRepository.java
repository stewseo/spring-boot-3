package org.example.spring.r2dbc.tests;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Interface definition that declares:<br>
 * - The name for our Spring Data repository<br>
 * - Spring Data Commons’ base interface for any reactive repository, ReactiveCrudRepository as it's parent interface<br>
 * - The domain and primary key’s type for this repository
 */
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {}
