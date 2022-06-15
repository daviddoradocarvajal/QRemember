package com.iesgala.qremember.model;

import java.io.Serializable;

/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class Categoria implements Serializable {
    private final String nombre;

    public Categoria(String nombre){
        this.nombre = nombre;

    }

    public String getNombre() {
        return nombre;
    }

}
