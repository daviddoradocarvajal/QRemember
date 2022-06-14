package com.iesgala.qremember.model;

import java.io.Serializable;

public class BasicLugar implements Serializable {
    private float longitud;
    private float latitud;
    private float altitud;
    private String enlace;
    private String nombre;

    public BasicLugar(float longitud, float latitud, float altitud, String enlace, String nombre) {
        this.longitud = longitud;
        this.latitud = latitud;
        this.altitud = altitud;
        this.enlace = enlace;
        this.nombre = nombre;
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
}
