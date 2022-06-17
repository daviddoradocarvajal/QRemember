package com.iesgala.qremember.model;
/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class LugarCompartido {

    private final String enlace;
    private final String longitud;
    private final String latitud;
    private final String altitud;
    private final String emailEmisor;
    private final String emailReceptor;


    public LugarCompartido(String enlace, String longitud, String latitud, String altitud, String emailEmisor, String emailReceptor) {
        this.enlace = enlace;
        this.longitud = longitud;
        this.latitud = latitud;
        this.altitud = altitud;
        this.emailEmisor = emailEmisor;
        this.emailReceptor = emailReceptor;
    }

    public String getEnlace() {
        return enlace;
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

    public String getEmailEmisor() {
        return emailEmisor;
    }

    public String getEmailReceptor() {
        return emailReceptor;
    }
}
