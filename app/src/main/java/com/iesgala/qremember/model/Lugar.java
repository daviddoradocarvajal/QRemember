package com.iesgala.qremember.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.sql.Date;


/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class Lugar implements Serializable {
    private float longitud;
    private float latitud;
    private float altitud;
    private String enlace;
    private String nombre;
    private Date fecha;
    private Imagen principal;
    private Imagen[] imagenes;
    private Categoria[] categorias;

    public Lugar(float longitud, float latitud, float altitud, String enlace, String nombre, Date fecha, Imagen principal, @Nullable Imagen[] imagenes, Categoria[] categorias) {
        this.longitud = longitud;
        this.latitud = latitud;
        this.altitud = altitud;
        this.enlace = enlace;
        this.nombre = nombre;
        this.fecha = fecha;
        this.principal = principal;
        this.imagenes = imagenes;
        this.categorias = categorias;
    }

    public float getLongitud() {
        return longitud;
    }

    public float getLatitud() {
        return latitud;
    }

    public float getAltitud() {
        return altitud;
    }

    public String getEnlace() {
        return enlace;
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public Imagen getPrincipal() {
        return principal;
    }

    public Imagen[] getImagenes() {
        return imagenes;
    }

    public Categoria[] getCategorias() {
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
