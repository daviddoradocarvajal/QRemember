package com.iesgala.qremember.model;

public class LugarUsuario {

    private String enlace;
    private String longitud;
    private String latitud;
    private String altitud;
    private String emailEmisor;
    private String emailReceptor;


    public LugarUsuario(String enlace, String longitud, String latitud, String altitud, String emailEmisor, String emailReceptor) {
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
