package com.iesgala.qremember.model;


/**
 * Clase que representa una categoria del modelo de datos
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class Categoria {
    private final String nombre;

    public Categoria(String nombre){
        this.nombre = nombre;

    }

    public String getNombre() {
        return nombre;
    }

}
