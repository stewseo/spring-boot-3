package org.example.spring.webservice.model;

import org.example.spring.webservice.model.VideoEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CoreDomainTest {

  // Verifies that when we create a new instance of VideoEntity, its id field should be null.
  @Test
  void newVideoEntityShouldHaveNullId() {
    VideoEntity entity = new VideoEntity("alice", "title", "description");
    assertThat(entity.getId()).isNull();
    assertThat(entity.getUsername()).isEqualTo("alice");
    assertThat(entity.getName()).isEqualTo("title");
    assertThat(entity.getDescription()) //
      .isEqualTo("description");
  }

  // Verifies whether the value of the toString() method has the expected value.
  @Test
  void toStringShouldAlsoBeTested() {
    VideoEntity entity = new VideoEntity("alice", "title", "description");
    assertThat(entity.toString())
      .isEqualTo("VideoEntity{id=null, username='alice', name='title', description='description'}");
  }

  // Verifies the entity’s setter methods
  @Test
  void settersShouldMutateState() {
    VideoEntity entity = new VideoEntity("alice", "title", "description");
    entity.setId(99L);
    entity.setName("new name");
    entity.setDescription("new desc");
    entity.setUsername("bob");

    // verifies that the state was mutated properly
    assertThat(entity.getId()).isEqualTo(99L);
    assertThat(entity.getUsername()).isEqualTo("bob");
    assertThat(entity.getName()).isEqualTo("new name");
    assertThat(entity.getDescription()) //
      .isEqualTo("new desc");
  }
}
