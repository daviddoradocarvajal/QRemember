package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.CompartirRutaActivity;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/**
 * Clase controladora de la actividad PopupRutaActivity maneja los eventos de los botones
 * eliminar ruta y compartir ruta
 * @author David Dorado
 * @version 1.0
 */
public class PopupRutaController {
    /**
     * Método encargado de responder al click sobre el botón eliminar ruta, muestra un cuadro de
     * diálogo que si se acepta elimina la ruta de la cuenta del usuario y se se rechaza vuelve a
     * la lista de rutas
     * @param activity Actividad que lanza el evento
     * @param emailUsuario Email del usuario
     * @param nombreRuta Nombre de la ruta
     */
    public static void eliminarRuta(Activity activity, String emailUsuario, String nombreRuta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.msg_aviso));
        builder.setMessage(activity.getString(R.string.seguro_eliminar_ruta));
        builder.setPositiveButton(activity.getString(R.string.confirmar), (dialog, which) -> {
            try {
                new AsyncTasks.DeleteTask().execute("DELETE FROM ruta WHERE nombre='"+nombreRuta+"' AND email_usuario='"+emailUsuario+"';").get(1, TimeUnit.MINUTES);
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.putExtra(Utils.INTENTS_EMAIL_EMISOR,emailUsuario);
            activity.finish();
        });
        builder.setNegativeButton(activity.getString(R.string.cancelar), (dialog, which) -> {
            Intent intent = new Intent();
            intent.putExtra(Utils.INTENTS_EMAIL_EMISOR,emailUsuario);
            activity.finish();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Método que lanza un intent a la clase CompartirRutaActivity con los datos de la ruta a fin
     * de compartirla con otro usuario cuando se pulsa sobre el botón compartir ruta
     * @param activity Actividad que lanza el evento
     * @param emailUsuario Email del usuario que comparte la ruta
     * @param nombreRuta Nombre de la ruta a compartir
     */
    public static void compartirRuta(Activity activity, String emailUsuario, String nombreRuta) {
        Intent intent = new Intent(activity, CompartirRutaActivity.class);
        intent.putExtra(Utils.INTENTS_NOMBRE_RUTA,nombreRuta);
        intent.putExtra(Utils.INTENTS_EMAIL_EMISOR,emailUsuario);
        activity.startActivity(intent);
    }
}
