package org.example.spring.data.jpa.queries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Interface extending JpaRepository with two generic parameters: VideoEntity and Long (the domain type and the primary key type)
 * JpaRepository, a Spring Data JPA interface, contains a set of already supported Change Replace Update Delete (CRUD) operations
  */
public interface  VideoRepository extends JpaRepository<VideoEntity, Long> {
  /**
   * Custom finder method that Spring Data implements by parsing the method name.
   * @param partialName
   * @return a List of type <Video Entity>, indicating it must return a list of the repository’s domain type.
   */
  List<VideoEntity> findByNameContainsIgnoreCase(String partialName);

  List<VideoEntity> findByDescriptionContainsIgnoreCase(String partialDescription);

  List<VideoEntity> findByNameContainsOrDescriptionContainsAllIgnoreCase(String partialName,
    String partialDescription);

  /**
   * Supplies a custom JPQL statement that joins four different tables together, using standard inner joins.<br>
   * Binds named parameters :minimumViews and :minimumLikes to the method arguments by the Spring Data @Param("minimumViews") and @Param("minimumLikes") annotations.<br>
   * (instead of the default positional parameters)
   * @return List<VideoEntity>, Spring Data will form a collection.
   */

  @Query(
          """
          select v FROM VideoEntity v
          JOIN v.metrics m
          JOIN m.activity a
          JOIN v.engagement e
          WHERE a.views < :minimumViews
          OR e.likes < :minimumLikes
          """
  )
  List<VideoEntity> findVideosThatArentPopular(//
                                                @Param("minimumViews") Long minimumViews, //
                                                @Param("minimumLikes") Long minimumLikes);
}
