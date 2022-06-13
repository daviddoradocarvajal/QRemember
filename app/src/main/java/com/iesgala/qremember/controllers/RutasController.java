package com.iesgala.qremember.controllers;

import android.app.Activity;

import com.iesgala.qremember.model.Ruta;

import java.util.ArrayList;

public class RutasController {

    public static ArrayList<Ruta> obtenerRutas(Activity activity,String emailUsuario){
        ArrayList<Ruta> rutas = new ArrayList<>();
        //SELECT nombre,email FROM ruta where email=emailUsuario
        // if rs
        //SELECT * FROM lugar_ruta where nombre_ruta=rs.nombre and email_ruta=emailUsuario
        // if rs
        // basiclugares.add(lugar)
        // SELECT * FROM ruta_categoria where nombre_ruta=rs.nombre and email_ruta=emailUsuario
        // categorias.add(categoria)
        // rutas.add(new ruta(nombre,email,basiclugares,categorias)
        return rutas;
    }

    public static ArrayList<Ruta> obtenerRutasFiltradas(Activity activity,String emailUsuario, String categoria){
        ArrayList<Ruta> rutas = new ArrayList<>();
        //SELECT nombre,email FROM ruta where email=emailUsuario
        // if rs
        //SELECT * FROM lugar_ruta where nombre_ruta=rs.nombre and email_ruta=emailUsuario
        // if rs
        // basiclugares.add(lugar)
        // SELECT * FROM ruta_categoria where nombre_ruta=rs.nombre and email_ruta=emailUsuario and nombre_categoria=categoria
        // categorias.add(categoria)
        // rutas.add(new ruta(nombre,email,basiclugares,categorias)
        return rutas;
    }

    public static void verRuta(Activity activity, String nombreRuta, String emailUsuario){
        // Start activity verRuta

    }

    public static void nuevaRuta(Activity activity,String emailUsuario){
        // Intent put emailUsuario
        // Start activity nuevaRuta
    }
}
