package com.iesgala.qremember.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;


/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class Lugar implements Serializable {
    private String longitud;
    private String latitud;
    private String altitud;
    private String enlace;
    private String nombre;
    private ArrayList<Imagen> imagenes;
    private ArrayList<Categoria> categorias;

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
