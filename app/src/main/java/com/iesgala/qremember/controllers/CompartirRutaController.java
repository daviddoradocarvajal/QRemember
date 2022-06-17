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
 * Clase controladora de la actividad CompartirRutaActivity maneja el evento de compartir una
 * ruta con otro usuario
 * @author David Dorado
 * @version 1.0
 */
public class CompartirRutaController {
    /**
     * Método que responde al click sobre el botón compartir
     * @param activity Actividad donde se lanza el evento
     * @param emailReceptor Email del usuario con el cual compartir la ruta
     * @param emailEmisor Email del usuario logueado en la aplicación el cual comparte la ruta
     * @param nombreRuta Nombre de la ruta a compartir
     */
    public static void compartirRuta(Activity activity, String emailEmisor,String emailReceptor,String nombreRuta){
        try {
            /*
             * Se comprueba si la ruta está ya registrada en la cuenta del usuario con el cual se
             * quiere compartir
             */
            ResultSet resultSetComprobacionYaRegistrado = new AsyncTasks.SelectTask().execute("SELECT * FROM ruta WHERE email_usuario = '"+emailReceptor+"' AND nombre='"+nombreRuta+"'").get(1, TimeUnit.MINUTES);
            if(resultSetComprobacionYaRegistrado.next()){
                Toast.makeText(activity, activity.getString(R.string.msg_ruta_repetida),Toast.LENGTH_LONG).show();
            }else {
                //Se comprueba que la ruta no ha sido compartida ya con el usuario
                ResultSet resultSetComprobacionYaCompartido = new AsyncTasks.SelectTask().execute("SELECT * FROM `rutas_compartidas` WHERE usuario_emisor='"+emailEmisor+"' AND nombre_ruta='"+nombreRuta+"' AND email_ruta='"+emailEmisor+"' AND usuario_receptor='"+emailReceptor+"'").get(1,TimeUnit.MINUTES);
                if (resultSetComprobacionYaCompartido.next()) {
                    Toast.makeText(activity, activity.getString(R.string.msg_ruta_ya_compartida),Toast.LENGTH_LONG).show();
                } else {
                    // Se inserta el registro en la base de datos
                    if (new AsyncTasks.InsertTask().execute("INSERT INTO rutas_compartidas(usuario_emisor, nombre_ruta, email_ruta, usuario_receptor) VALUES ('"+emailEmisor+"','"+nombreRuta+"','"+emailEmisor+"','"+emailReceptor+"')").get(1, TimeUnit.MINUTES)) {
                        Toast.makeText(activity, activity.getString(R.string.msg_compartida_ok),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity,activity.getString(R.string.err_desconocido),Toast.LENGTH_LONG).show();
                    }
                }
            }
            Intent intent = new Intent();
            intent.putExtra(Utils.INTENTS_EMAIL,emailEmisor);
            activity.finish();
        } catch (ExecutionException | InterruptedException | TimeoutException | SQLException e) {
            e.printStackTrace();
        }
    }
}
