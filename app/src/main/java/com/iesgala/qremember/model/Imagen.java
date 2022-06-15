package com.iesgala.qremember.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class Imagen implements Serializable {
    private final int id;
    private final Drawable imagen;
    private boolean seleccionado;

    public Imagen(int id, Drawable imagen) {
        this.id = id;
        this.imagen = imagen;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public int getId() {
        return id;
    }

    public Drawable getImagen() {
        return imagen;
    }
}
