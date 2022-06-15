package com.iesgala.qremember.model;

import java.io.Serializable;

public class BasicLugar implements Serializable {
    private final float longitud;
    private final float latitud;
    private final float altitud;
    private final String enlace;
    private final String nombre;

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
