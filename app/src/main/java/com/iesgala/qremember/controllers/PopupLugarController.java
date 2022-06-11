package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.iesgala.qremember.R;
import com.iesgala.qremember.model.Imagen;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class PopupLugarController {

    public static ArrayList<Imagen> obtenerImagenes(Activity activity,String enlace){
        ArrayList<Imagen> imagenes = new ArrayList<>();
        try{
            ResultSet resultSet = new AsyncTasks.SelectTask().execute("SELECT id, imagen FROM imagen where imagen.enlace = '"+enlace+"'").get(1, TimeUnit.MINUTES);
            if(resultSet!=null){
                while(resultSet.next()){
                    InputStream stream = resultSet.getBlob("imagen").getBinaryStream();
                    imagenes.add(new Imagen(resultSet.getInt("id"), Drawable.createFromStream(stream, "imagen")));
                }
            }
        }catch (Exception e){
            Utils.AlertDialogGenerate(activity,activity.getString(R.string.err),e.getMessage());
        }
        return imagenes;
    }
    public static void compartir(){

    }
    public static void modificar(){

    }
    public static void eliminar(){

    }
    public static void nuevaImagen(){

    }
    public static void eliminarImagen(ArrayList<Imagen> imagenes){
        for (Imagen imagen:imagenes){
            System.out.println(imagen.isSeleccionado());
        }

    }
}
