
package com.vitalloop;

public class Donor {

    private final int donorId;
    private final String bloodGroup;
    private final String city;

    public Donor(int donorId, String bloodGroup, String city) {
        this.donorId = donorId;
        this.bloodGroup = bloodGroup;
        this.city = city;
    }

    public int getDonorId() {
        return donorId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getCity() {
        return city;
    }
}
