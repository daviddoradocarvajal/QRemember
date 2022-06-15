package com.iesgala.qremember.model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class Lugar implements Serializable {
    private final String longitud;
    private final String latitud;
    private final String altitud;
    private final String enlace;
    private final String nombre;
    private final ArrayList<Imagen> imagenes;
    private final ArrayList<Categoria> categorias;

    public Lugar(String longitud, String latitud, String altitud, String enlace, String nombre, ArrayList<Imagen> imagenes, ArrayList<Categoria> categorias) {
        this.longitud = longitud;
        this.latitud = latitud;
        this.altitud = altitud;
        this.enlace = enlace;
        this.nombre = nombre;
        this.imagenes = imagenes;
        this.categorias = categorias;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getAltitud() {
        return altitud;
    }

    public String getEnlace() {
        return enlace;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Imagen> getImagenes() {
        return imagenes;
    }

    public ArrayList<Categoria> getCategorias() {
        return categorias;
    }

    public String getTvCategorias() {
        String resultado ="";
        for (Categoria categoria : categorias){
            resultado += categoria.getNombre() + "\n";
        }
        return resultado;
    }
}
