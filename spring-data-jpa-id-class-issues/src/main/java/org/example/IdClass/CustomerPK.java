package org.example.IdClass;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerPK implements Serializable {

    private Long unitId;

    private Long versionId;

}
