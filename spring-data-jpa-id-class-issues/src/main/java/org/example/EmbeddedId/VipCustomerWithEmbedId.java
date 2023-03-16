package org.example.EmbeddedId;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("vip")
public class VipCustomerWithEmbedId extends CustomerWithEmbedId {
    private String vipNumber;

    public VipCustomerWithEmbedId() {
    }

    public VipCustomerWithEmbedId(String firstName, String lastName, String vipNumber) {
        super(firstName, lastName);
        this.vipNumber = vipNumber;
    }

    public String getVipNumber() {
        return vipNumber;
    }

    public void setVipNumber(String vipNumber) {
        this.vipNumber = vipNumber;
    }
}
