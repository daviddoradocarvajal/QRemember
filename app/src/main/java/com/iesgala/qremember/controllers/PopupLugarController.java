package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.CompartirLugarActivity;
import com.iesgala.qremember.activities.ModificarLugarActivity;
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
 * Clase controladora de la actividad PopupLugarActivity maneja los eventos sobre la ventana que
 * aparece al pulsar sobre un lugar de la lista de lugares de un usuario
 * @author David Dorado
 * @version 1.0
 */
public class PopupLugarController {
    /**
     * Método para obtener una lista con todas las imágenes del lugar
     * @param activity Actividad que ejecuta el método
     * @param enlace enlace que identifica al lugar del cual obtener las imágenes
     * @return Una lista con objetos de la clase Imagen
     */
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

    /**
     * Método que responde al evento de click sobre el botón Compartir, lanza la actividad
     * CompartirLugarActivity enviandole en el intent los datos del lugar para compartir
     * @param activity Actividad que lanza el evento
     * @param emailEmisor Email del usuario que comparte
     * @param enlace Enlace del lugar a compartir
     * @param longitud Longitud del lugar para las coordenadas, se usa para identificar el lugar
     * @param latitud Latitud del lugar para las coordenadas, se usa para identificar el lugar
     * @param altitud Altitud del lugar para las coordenadas, se usa para identificar el lugar
     */
    public static void compartir(Activity activity,String emailEmisor,String enlace,String longitud,String latitud,String altitud){
        Intent intent = new Intent(activity.getBaseContext(), CompartirLugarActivity.class);
        intent.putExtra(Utils.INTENTS_ENLACE, enlace);
        intent.putExtra(Utils.INTENTS_LONGITUD, longitud);
        intent.putExtra(Utils.INTENTS_LATITUD, latitud);
        intent.putExtra(Utils.INTENTS_ALTITUD, altitud);
        intent.putExtra(Utils.INTENTS_EMAIL_EMISOR, emailEmisor);
        activity.startActivity(intent);
        activity.finish();

    }

    /**
     * Método que responde al evento de click sobre el botón Modificar, lanza la actividad
     * ModificarLugarActivity enviando los datos del lugar en un Intent
     * @param activity Actividad que lanza el evento
     * @param enlace Enlace del lugar
     * @param longitud Longitud del lugar para las coordenadas, se usa para identificar el lugar
     * @param latitud Latitud del lugar para las coordenadas, se usa para identificar el lugar
     * @param altitud Altitud del lugar para las coordenadas, se usa para identificar el lugar
     * @param nombre Nomnbre del lugar
     * @param email Email del usuario
     */
    public static void modificar(Activity activity,String enlace,String longitud,String latitud,String altitud,String nombre,String email){
        Intent intent = new Intent(activity.getBaseContext(), ModificarLugarActivity.class);
        intent.putExtra(Utils.INTENTS_ENLACE, enlace);
        intent.putExtra(Utils.INTENTS_LONGITUD, longitud);
        intent.putExtra(Utils.INTENTS_LATITUD, latitud);
        intent.putExtra(Utils.INTENTS_ALTITUD, altitud);
        intent.putExtra(Utils.INTENTS_NOMBRE_LUGAR, nombre);
        intent.putExtra(Utils.INTENTS_EMAIL,email);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * Método que responde al evento de click sobre el botón Eliminar, muestra un cuadro de diálogo
     * que si se acepta elimina el lugar de la cuenta del usuario sin eliminarlo de la base de datos
     * y si se rechaza vuelve a la lista de lugares
     * @param activity Actividad que lanza el evento
     * @param enlace Enlace del lugar a eliminar
     * @param email Email del usuario
     */
    public static void eliminarLugar(Activity activity, String enlace, String email){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.msg_aviso));
        builder.setMessage(activity.getString(R.string.seguro_eliminar_lugar));
        builder.setPositiveButton(activity.getString(R.string.confirmar), (dialog, which) -> {
            try {
                new AsyncTasks.DeleteTask().execute("DELETE FROM lugar_usuario WHERE enlace='"+enlace+"' AND email_usuario='"+email+"';").get(1, TimeUnit.MINUTES);
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.putExtra(Utils.INTENTS_EMAIL_EMISOR, email);
            activity.finish();
        });
        builder.setNegativeButton(activity.getString(R.string.cancelar), (dialog, which) -> {
            Intent intent = new Intent();
            intent.putExtra(Utils.INTENTS_EMAIL_EMISOR, email);
            activity.finish();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Método que responde al evento de click sobre el botón Nueva Imagen, realiza una foto y la
     * guarda en las imágenes del lugar
     * @param activity Actividad que lanza el evento
     */
    public static void nuevaImagen(Activity activity){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, NuevoLugarActivity.REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Método que responde al evento de click sobre el botón Eliminar Imágenes, si hay al menos
     * una imagen seleccionada pregunta en un cuadro de diálogo si se desean eliminar y si se acepta
     * elimina las imagenes de la base de datos, si se rechaza vuelve a la lista de lugares
     * @param activity Actividad que lanza el evento
     * @param imagenes Lista con las imagenes a eliminar
     * @param email Email del usuario
     */
    public static void eliminarImagenes(Activity activity, ArrayList<Imagen> imagenes, String email){
        int contador=0;
        // Se comprueba que al menos una imagen esté seleccionada
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
                Intent intent = new Intent();
                intent.putExtra(Utils.INTENTS_EMAIL_EMISOR, email);
                activity.finish();
            });
            builder.setNegativeButton(activity.getString(R.string.cancelar), (dialog, which) -> {
                Intent intent = new Intent();
                intent.putExtra(Utils.INTENTS_EMAIL_EMISOR, email);
                activity.finish();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
