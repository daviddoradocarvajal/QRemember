package com.iesgala.qremember.model;
/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class Ruta {
    private String nombre;
    private Usuario usuario;
    private Lugar[] lugares;

    public Ruta(String nombre, Usuario usuario, Lugar[] lugares) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.lugares = lugares;
    }
}
