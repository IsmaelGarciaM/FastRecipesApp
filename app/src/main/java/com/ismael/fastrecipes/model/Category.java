package com.ismael.fastrecipes.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Ismael on 23/01/2018.
 */

public class Category {
    private String name;
    private Drawable img;
    private boolean state;

    public Category(String name, Drawable img, boolean state) {
        this.name = name;
        this.img = img;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

}
