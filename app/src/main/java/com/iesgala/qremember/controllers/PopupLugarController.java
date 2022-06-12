package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.NuevoLugarActivity;
import com.iesgala.qremember.model.Imagen;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    public static void compartir(Activity activity,String email){
        //usuario_emisor 	latitud 	longitud 	altitud 	enlace
        // Start activity compartir lugar
        // finish

    }
    public static void modificar(Activity activity,String enlace){
        //String longitud, String latitud, String altitud, String enlace, String nombre
        // start activity modificar
        // finish
    }
    public static void eliminar(Activity activity,String enlace){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.msg_aviso));
        builder.setMessage(activity.getString(R.string.seguro_eliminar_lugar));
        builder.setPositiveButton(activity.getString(R.string.confirmar), (dialog, which) -> {
            try {
                new AsyncTasks.DeleteTask().execute("DELETE FROM lugar WHERE enlace='"+enlace+"';").get(1, TimeUnit.MINUTES);
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
            activity.finish();
        });
        builder.setNegativeButton(activity.getString(R.string.cancelar), (dialog, which) -> activity.finish());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void nuevaImagen(Activity activity,String enlace,String longitud,String latitud,String altitud){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, NuevoLugarActivity.REQUEST_IMAGE_CAPTURE);
        }
    }
    public static void eliminarImagen(Activity activity,ArrayList<Imagen> imagenes){
        int contador=0;
        for (Imagen img:imagenes){
            if(img.isSeleccionado()) contador++;
        }
        if(contador==0){
            Utils.AlertDialogGenerate(activity,activity.getString(R.string.err),activity.getString(R.string.err_no_imagen));
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(activity.getString(R.string.msg_aviso));
            builder.setMessage(activity.getString(R.string.msg_eliminar));
            builder.setPositiveButton(activity.getString(R.string.confirmar), (dialog, which) -> {
                for (Imagen imagen : imagenes) {
                    try {
                        if (imagen.isSeleccionado())
                            new AsyncTasks.DeleteTask().execute("DELETE FROM imagen where id=" + imagen.getId()).get(1, TimeUnit.MINUTES);
                    } catch (ExecutionException | InterruptedException | TimeoutException e) {
                        e.printStackTrace();
                    }
                }
                activity.finish();
            });
            builder.setNegativeButton(activity.getString(R.string.cancelar), (dialog, which) -> activity.finish());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
