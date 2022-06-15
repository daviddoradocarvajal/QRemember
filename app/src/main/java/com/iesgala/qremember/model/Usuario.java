package com.iesgala.qremember.model;

import java.io.Serializable;

/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class Usuario implements Serializable {
    private String nombre;
    private final String email;
    private final String contrasenia;
    private String pregunta;
    private String respuesta;
    private Lugar[] lugares;
    private Ruta[] rutas;


    public Usuario (String nombre, String email, String contrasenia){
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
    }
    public Usuario (String email, String contrasenia){
        this.email = email;
        this.contrasenia = contrasenia;
    }


    public String getEmail() {
        return email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
