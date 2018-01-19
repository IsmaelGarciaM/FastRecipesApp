package com.ismael.fastrecipes.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Ismael on 18/01/2018.
 * @author Ismael Garcia
 * @version 0.0.1
 * Clase entidad para un ingrediente
 */

@Entity
public class ShopList {

    @PrimaryKey
    private int id;
    private String name;
    private float units;

    public void setName(String name) {
        this.name = name;
    }

    public void setUnits(float units) {
        this.units = units;
    }

    public void setId(int id){
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getUnits() {
        return units;
    }



    public ShopList(int id, String name, float units) {
        this.id = id;
        this.name = name;
        this.units = units;
    }
}
