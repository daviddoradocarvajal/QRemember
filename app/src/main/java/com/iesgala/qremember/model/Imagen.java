package com.iesgala.qremember.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class Imagen implements Serializable {
    private int id;
    private Drawable imagen;

    public Imagen(int id, Drawable imagen) {
        this.id = id;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public Drawable getImagen() {
        return imagen;
    }
}
