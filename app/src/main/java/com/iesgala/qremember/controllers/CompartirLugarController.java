package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.iesgala.qremember.R;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Clase controladora de la actividad CompartirLugarActivity maneja el evento de compartir un
 * lugar con otro usuario
 * @author David Dorado
 * @version 1.0
 */
public class CompartirLugarController {
    /**
     * Método que responde al click sobre el botón compartir
     * @param activity Actividad donde se lanza el evento
     * @param emailReceptor Email del usuario con el cual compartir el lugar
     * @param emailEmisor Email del usuario logueado en la aplicación el cual comparte el lugar
     * @param enlace Cadena de caracteres con el enlace almacenado en el código QR del lugar
     * @param longitud Longitud del lugar para las coordenadas
     * @param latitud Latitud del lugar para las coordenadas
     * @param altitud Altitud del lugar para las coordenadas
     */
    public static void compartir(Activity activity,String emailReceptor,String emailEmisor,String enlace,String longitud,String latitud,String altitud){
        try {
            /*
             * Se comprueba si el lugar está ya registrado en la cuenta del usuario con el cual se
             * quiere compartir el lugar
             */
            ResultSet resultSetComprobacionYaRegistrado = new AsyncTasks.SelectTask().execute("SELECT * FROM lugar_usuario WHERE email_usuario = '"+emailReceptor+"' AND enlace='"+enlace+"'").get(1, TimeUnit.MINUTES);
            if(resultSetComprobacionYaRegistrado.next()){
                Toast.makeText(activity, activity.getString(R.string.msg_ya_registrado),Toast.LENGTH_LONG).show();
            }else {
                //Se comprueba que el lugar no ha sido compartido ya con el usuario
                ResultSet resultSetComprobacionYaCompartido = new AsyncTasks.SelectTask().execute("SELECT * FROM `lugares_compartidos` WHERE enlace='"+enlace+"' AND usuario_receptor='"+emailReceptor+"'").get(1,TimeUnit.MINUTES);
                if (resultSetComprobacionYaCompartido.next()) {
                    Toast.makeText(activity, activity.getString(R.string.msg_ya_compartido),Toast.LENGTH_LONG).show();
                } else {
                    // Se inserta el registro en la base de datos
                    if (new AsyncTasks.InsertTask().execute("INSERT INTO lugares_compartidos (usuario_emisor, latitud, longitud, altitud, enlace, usuario_receptor) VALUES ('" + emailEmisor + "','" + latitud + "','" + longitud + "','" + altitud + "','" + enlace + "','" + emailReceptor + "')").get(1, TimeUnit.MINUTES)) {
                        Toast.makeText(activity, activity.getString(R.string.msg_compartido_ok),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity,activity.getString(R.string.err_desconocido),Toast.LENGTH_LONG).show();
                    }
                }
            }
            // Finalmente se finaliza la actividad devolviendo en un Intent el email del usuario
            Intent intent = new Intent();
            intent.putExtra(Utils.INTENTS_EMAIL,emailEmisor);
            activity.finish();
        } catch (ExecutionException | InterruptedException | TimeoutException | SQLException e) {
            e.printStackTrace();
        }
    }
}
