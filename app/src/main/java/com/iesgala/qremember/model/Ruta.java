package com.iesgala.qremember.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class Ruta implements Serializable {
    private String nombre;
    private String emailUsuario;
    private ArrayList<BasicLugar> lugares;
    private ArrayList<Categoria> categorias;

    public Ruta(String nombre, String emailUsuario, ArrayList<BasicLugar> lugares, ArrayList<Categoria> categorias) {
        this.nombre = nombre;
        this.emailUsuario = emailUsuario;
        this.lugares = lugares;
        this.categorias = categorias;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public ArrayList<BasicLugar> getLugares() {
        return lugares;
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
