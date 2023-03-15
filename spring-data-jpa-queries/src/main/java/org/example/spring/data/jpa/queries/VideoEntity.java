package org.example.spring.data.jpa.queries;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * This class is annotated as a JPA-managed type.
 * The flagged primary key is of type Long.
 * Primary key generation is offloaded to the JPA provider.
 * The protected no-argument constructor satisfies one of JPA's entity requirements
 * This class includes a constructor designed for creating new entries in the database where the id field isn’t provided.
 *  When the id field is null, it tells JPA we want to create a new row in the table.
 */
@Entity
class VideoEntity {

  private @Id @GeneratedValue Long id;
  private String name;
  private String description;

  protected VideoEntity() {
    this(null, null);
  }

  VideoEntity(String name, String description) {
    this.id = null;
    this.description = description;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
