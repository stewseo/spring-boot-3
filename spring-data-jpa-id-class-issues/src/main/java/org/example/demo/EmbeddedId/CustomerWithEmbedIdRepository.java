package org.example.demo.EmbeddedId;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerWithEmbedIdRepository extends CrudRepository<CustomerWithEmbedId, CustomerPK> {


}
