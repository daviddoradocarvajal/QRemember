package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.iesgala.qremember.R;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Clase controladora de la actividad ModificarLugarActivity maneja el evento para modificar un lugar
 * @author David Dorado
 * @version 1.0
 */
public class ModificarLugarController {
    /**
     * Método que responde al evento de click del botón modificar para modificar el lugar con los
     * datos recibidos como parámetros nombreLugar, enlace, longitud, latitud, altitud en la cuenta
     * del usuario con el mismo email que el recibido como último parámetro. Se modifican las categorias
     * por las que se reciben el la lista seleccionadas que se recibe como segundo parámetro y si el
     * parámetro nombre lugar no viene en blanco se modifica también el nombre del lugar
     * @param activity Actividad que lanza el evento
     * @param seleccionadas Lista con los nombres de las categorias seleccionadas
     * @param nombreLugar Cadena con el nuevo nombre del lugar o vacio si no se va a modificar
     * @param enlace Cadena con el enlace del lugar
     * @param longitud Longitud del lugar para las coordenadas, se usa para identificar el lugar a
     *                 modificar
     * @param latitud Latitud del lugar para las coordenadas, se usa para identificar el lugar a
     *                modificar
     * @param altitud Altitud del lugar para las coordenadas, se usa para identificar el lugar a
     *                modificar
     * @param email Cadena con el email del usuario
     */
    public static void modificarLugar(Activity activity, ArrayList<String> seleccionadas, String nombreLugar, String enlace, String longitud, String latitud, String altitud,String email){
        try {
            if (nombreLugar != null && !nombreLugar.isEmpty()) {
                if (new AsyncTasks.UpdateTask().execute("UPDATE lugar SET nombre='"+nombreLugar+"' WHERE enlace='"+enlace+"'").get(1, TimeUnit.MINUTES)){
                    if(new AsyncTasks.DeleteTask().execute("DELETE FROM lugar_categoria WHERE enlace='"+enlace+"'").get(1,TimeUnit.MINUTES)){
                        for (String seleccionada : seleccionadas) {
                            new AsyncTasks.InsertTask().execute("INSERT INTO lugar_categoria (longitud, latitud, altitud, enlace, nombre_categoria) VALUES ('"+longitud+"','"+latitud+"','"+altitud+"','"+enlace+"','"+seleccionada+"')").get(1,TimeUnit.MINUTES);
                        }
                    }else
                        Toast.makeText(activity, activity.getString(R.string.err_desconocido), Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(activity, activity.getString(R.string.err_desconocido), Toast.LENGTH_LONG).show();
                activity.finish();
            } else {
                if(new AsyncTasks.DeleteTask().execute("DELETE FROM lugar_categoria WHERE enlace='"+enlace+"'").get(1,TimeUnit.MINUTES)){
                    for (String seleccionada : seleccionadas) {
                        new AsyncTasks.InsertTask().execute("INSERT INTO lugar_categoria (longitud, latitud, altitud, enlace, nombre_categoria) VALUES ('"+longitud+"','"+latitud+"','"+altitud+"','"+enlace+"','"+seleccionada+"')").get(1,TimeUnit.MINUTES);
                    }
                }else
                    Toast.makeText(activity, activity.getString(R.string.err_desconocido), Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent();
            intent.putExtra(Utils.INTENTS_EMAIL,email);
            activity.finish();
        }catch (InterruptedException | ExecutionException | TimeoutException e){
                e.printStackTrace();
        }
    }
}
