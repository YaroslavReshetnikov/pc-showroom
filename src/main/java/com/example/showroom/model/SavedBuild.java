package com.example.showroom.model;

public class SavedBuild {

    private Build build;
    private double total; // 🔥 змінили назву

    public SavedBuild(Build build, double total) {
        this.build = build;
        this.total = total;
    }

    public Build getBuild() {
        return build;
    }

    public double getTotal() {  // 🔥 тепер співпадає з HTML
        return total;
    }
}