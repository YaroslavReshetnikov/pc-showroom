package com.example.showroom.model;

public class PickupOrder {

    private String customerName;
    private String phone;
    private String showroomAddress;
    private SavedBuild build;

    public PickupOrder() {
    }

    public PickupOrder(String customerName, String phone, String showroomAddress, SavedBuild build) {
        this.customerName = customerName;
        this.phone = phone;
        this.showroomAddress = showroomAddress;
        this.build = build;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getShowroomAddress() {
        return showroomAddress;
    }

    public SavedBuild getBuild() {
        return build;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setShowroomAddress(String showroomAddress) {
        this.showroomAddress = showroomAddress;
    }

    public void setBuild(SavedBuild build) {
        this.build = build;
    }
}