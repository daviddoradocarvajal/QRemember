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

public class PopupRutaController {

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

    public static void compartirRuta(Activity activity, String emailUsuario, String nombreRuta) {
        Intent intent = new Intent(activity, CompartirRutaActivity.class);
        intent.putExtra(Utils.INTENTS_NOMBRE_RUTA,nombreRuta);
        intent.putExtra(Utils.INTENTS_EMAIL_EMISOR,emailUsuario);
        activity.startActivity(intent);
    }
}
