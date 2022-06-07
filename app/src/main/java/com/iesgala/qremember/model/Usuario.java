package com.iesgala.qremember.model;

import java.io.Serializable;

/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class Usuario implements Serializable {
    private String nombre;
    private String email;
    private String contrasenia;
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


    public Usuario(String nombre, String email, String contrasenia, Lugar[] lugares, Ruta[] rutas) {
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.lugares = lugares;
        this.rutas = rutas;
    }
    public Usuario(String nombre, String email, String contrasenia, String pregunta, String respuesta, Lugar[] lugares, Ruta[] rutas) {
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.lugares = lugares;
        this.rutas = rutas;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Lugar[] getLugares() {return lugares;}

    public void setLugares(Lugar[] lugares) {this.lugares = lugares;}

    public Ruta[] getRutas() {return rutas;}

    public void setRutas(Ruta[] rutas) {this.rutas = rutas;}

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
