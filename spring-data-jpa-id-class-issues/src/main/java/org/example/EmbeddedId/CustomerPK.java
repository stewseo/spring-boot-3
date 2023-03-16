package org.example.EmbeddedId;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CustomerPK implements Serializable {

    private Long unitId;

    private Long versionId;

}
