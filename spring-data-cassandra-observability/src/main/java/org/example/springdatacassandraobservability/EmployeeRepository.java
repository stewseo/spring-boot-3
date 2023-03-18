package org.example.springdatacassandraobservability;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface EmployeeRepository extends CassandraRepository<Employee, String> {}
