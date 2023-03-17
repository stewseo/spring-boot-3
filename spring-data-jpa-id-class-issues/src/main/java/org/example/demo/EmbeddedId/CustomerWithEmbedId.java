package org.example.demo.EmbeddedId;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import org.hibernate.annotations.DiscriminatorFormula;

import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("case when vip_number is not null then 'vip' else 'normal' end")
@DiscriminatorValue("normal")
@Table(name = "customer_with_embedded_id")
public class CustomerWithEmbedId implements Serializable {

    //    @EmbeddedId
    @EmbeddedId
    private CustomerPK customerPK;

    private String firstName;
    private String lastName;

    protected CustomerWithEmbedId() {
    }

    public CustomerWithEmbedId(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setVersionId(Long versionId) {
        if (customerPK == null) {
            customerPK = new CustomerPK();
        }
        this.customerPK.setVersionId(versionId);
    }

    public void setUnitId(Long unitId) {
        if (customerPK == null) {
            customerPK = new CustomerPK();
        }
        this.customerPK.setUnitId(unitId);
    }
}
