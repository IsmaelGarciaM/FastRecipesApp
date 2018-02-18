package com.ismael.fastrecipes.model;

/**
 * Created by Ismael on 25/01/2018.
 */

public class Ingredient {
    private int id;
    private String name;
    private String measurement;

    public Ingredient(int id, String name, String measurement) {
        this.id = id;
        this.name = name;
        this.measurement = measurement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

}
