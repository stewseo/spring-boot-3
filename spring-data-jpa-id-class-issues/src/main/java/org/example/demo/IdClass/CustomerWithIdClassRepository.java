package org.example.demo.IdClass;

import org.springframework.data.repository.CrudRepository;

public interface CustomerWithIdClassRepository extends CrudRepository<CustomerWithIdClass, CustomerPK> {

}
