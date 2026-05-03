package com.example.showroom.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class OrderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderType;

    private String customerName;
    private String phone;
    private String city;

    private String deliveryMethod;
    private String showroomAddress;
    private String branchInfo;

    @Column(length = 5000)
    private String itemsSummary;

    private Double totalPrice;

    @Column(length = 2000)
    private String comment;

    private LocalDateTime createdAt;


    private String status;


    private Long buildRecordId;

    public OrderRecord() {
    }

    public Long getId() {
        return id;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public String getShowroomAddress() {
        return showroomAddress;
    }

    public String getBranchInfo() {
        return branchInfo;
    }

    public String getItemsSummary() {
        return itemsSummary;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public Long getBuildRecordId() {
        return buildRecordId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public void setShowroomAddress(String showroomAddress) {
        this.showroomAddress = showroomAddress;
    }

    public void setBranchInfo(String branchInfo) {
        this.branchInfo = branchInfo;
    }

    public void setItemsSummary(String itemsSummary) {
        this.itemsSummary = itemsSummary;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBuildRecordId(Long buildRecordId) {
        this.buildRecordId = buildRecordId;
    }
}