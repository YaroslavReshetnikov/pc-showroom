package com.example.showroom.model;

public class BuildView {

    private Long id;
    private String cpu;
    private String gpu;
    private String ram;
    private String mother;
    private String psu;
    private String cooling;
    private String other;
    private Double total;
    private String status;

    public BuildView() {
    }

    public Long getId() {
        return id;
    }

    public String getCpu() {
        return cpu;
    }

    public String getGpu() {
        return gpu;
    }

    public String getRam() {
        return ram;
    }

    public String getMother() {
        return mother;
    }

    public String getPsu() {
        return psu;
    }

    public String getCooling() {
        return cooling;
    }

    public String getOther() {
        return other;
    }

    public Double getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public void setPsu(String psu) {
        this.psu = psu;
    }

    public void setCooling(String cooling) {
        this.cooling = cooling;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}