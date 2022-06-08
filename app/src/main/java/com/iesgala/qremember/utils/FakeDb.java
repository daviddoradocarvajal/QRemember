package com.iesgala.qremember.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.Image;

import androidx.appcompat.content.res.AppCompatResources;

import com.iesgala.qremember.R;
import com.iesgala.qremember.model.Categoria;
import com.iesgala.qremember.model.Imagen;
import com.iesgala.qremember.model.Lugar;
import com.iesgala.qremember.model.Ruta;
import com.iesgala.qremember.model.Usuario;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Clase para simbolizar la base de datos falsa mientras arreglo los problemas de MYSQL
 */
public class FakeDb {
    public ArrayList<Categoria> categorias = new ArrayList<>();
    public ArrayList<Imagen> imagenes = new ArrayList<>();
    public ArrayList<Lugar> lugares = new ArrayList<>();
    public ArrayList<Ruta> rutas = new ArrayList<>();
    public ArrayList<Usuario> usuarios = new ArrayList<>();
    public FakeDb(Activity activity){
        // Obtención de los drawables
        Drawable hotel = AppCompatResources.getDrawable(activity,R.drawable.hotel);
        Drawable hotel2 = AppCompatResources.getDrawable(activity,R.drawable.hotel2);
        Drawable museo = AppCompatResources.getDrawable(activity,R.drawable.museo);
        Drawable museo2 = AppCompatResources.getDrawable(activity,R.drawable.museo2);
        Drawable restaurante = AppCompatResources.getDrawable(activity,R.drawable.restaurante);
        Drawable restaurante2 = AppCompatResources.getDrawable(activity,R.drawable.restaurante2);
        // Usuarios
        usuarios.add(new Usuario("Admin", "admin@qremember.es","admin"));
        usuarios.add(new Usuario("David", "david@qremember.es","david"));
        usuarios.add(new Usuario("Usuario2", "user@qremember.es", "userpass"));
        // Categorias
        categorias.add(new Categoria("Restaurantes"));
        categorias.add(new Categoria("Hoteles"));
        categorias.add(new Categoria("Museos"));
        // Imagenes
        imagenes.add(new Imagen(1,hotel));
        imagenes.add(new Imagen(2,hotel2));
        imagenes.add(new Imagen(3,museo));
        imagenes.add(new Imagen(4,museo2));
        imagenes.add(new Imagen(5,restaurante));
        imagenes.add(new Imagen(6,restaurante2));
        // Lugares
        lugares.add(new Lugar(77,15,4,"http://www.google.es",
                "Hotel 1",imagenes,categorias));
        lugares.add(new Lugar(78,28,2,"https://smartfenix.es/smart-fenix/","Hotel-Museo",
                imagenes,categorias ));
        lugares.add(new Lugar(77,20,2,"https://academiaandroid.com/proyecto-geolocalizacion-android/","Restaurante",
                imagenes,categorias));
        // Rutas
        rutas.add(new Ruta("Ruta 1",usuarios.get(1),new Lugar[]{lugares.get(0), lugares.get(1)}));
        rutas.add(new Ruta("Ruta 2",usuarios.get(2),new Lugar[]{lugares.get(2)}));
    }


}
