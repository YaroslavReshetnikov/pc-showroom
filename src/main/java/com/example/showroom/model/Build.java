package com.example.showroom.model;

public class Build {

    private Product cpu;
    private Product gpu;
    private Product ram;
    private Product mother;
    private Product other;
    private Product psu;
    private Product cooling;

    public Product getPsu() { return psu; }
    public void setPsu(Product psu) { this.psu = psu; }

    public Product getCooling() { return cooling; }
    public void setCooling(Product cooling) { this.cooling = cooling; }

    public Product getOther() { return other; }
    public void setOther(Product other) { this.other = other; }

    public Product getCpu() { return cpu; }
    public void setCpu(Product cpu) { this.cpu = cpu; }

    public Product getGpu() { return gpu; }
    public void setGpu(Product gpu) { this.gpu = gpu; }


    public Product getRam() { return ram; }
    public void setRam(Product ram) { this.ram = ram; }

    public Product getMother() { return mother; }
    public void setMother(Product mother) { this.mother = mother; }
}