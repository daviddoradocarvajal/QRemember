package com.iesgala.qremember.model;
/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class Categoria {
    private String nombre;
    private String descripcion;

    public Categoria(String nombre, String descripcion){
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
