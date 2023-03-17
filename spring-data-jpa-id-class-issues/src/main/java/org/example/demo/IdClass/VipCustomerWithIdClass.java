package org.example.demo.IdClass;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("vip")
public class VipCustomerWithIdClass extends CustomerWithIdClass {
    private String vipNumber;

    public VipCustomerWithIdClass() {
    }

    public VipCustomerWithIdClass(String firstName, String lastName, String vipNumber) {
        super(firstName, lastName);
        this.vipNumber = vipNumber;
    }

    public String getVipNumber() {
        return vipNumber;
    }

    public void setVipNumber(String vipNumber) {
        this.vipNumber = vipNumber;
    }

    @Override
    public String toString() {
        return "VipCustomerWithIdClass{" +
                "vipNumber='" + vipNumber + '\'' +
                "} " + super.toString();
    }
}
