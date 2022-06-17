package com.iesgala.qremember.model;
/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class RutaCompartida {
    private final String emailEmisor;
    private final String emailReceptor;
    private final String nombreRuta;

    public RutaCompartida(String emailEmisor, String emailReceptor, String nombreRuta) {
        this.emailEmisor = emailEmisor;
        this.emailReceptor = emailReceptor;
        this.nombreRuta = nombreRuta;
    }

    public String getEmailEmisor() {
        return emailEmisor;
    }

    public String getEmailReceptor() {
        return emailReceptor;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }
}
