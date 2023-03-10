package org.example.spring.data.jpa.queries;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Interface that extends JpaRepository with two generic parameters: VideoEntity and Long (the domain type and the primary key type)
// JpaRepository, a Spring Data JPA interface, contains a set of already supported Change Replace Update Delete (CRUD) operations
public interface  VideoRepository extends JpaRepository<VideoEntity, Long> {

  List<VideoEntity> findByNameContainsIgnoreCase(String partialName);

  List<VideoEntity> findByDescriptionContainsIgnoreCase(String partialDescription);

  List<VideoEntity> findByNameContainsOrDescriptionContainsAllIgnoreCase(String partialName,
    String partialDescription);

}
