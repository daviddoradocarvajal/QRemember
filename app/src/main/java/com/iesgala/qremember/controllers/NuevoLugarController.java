package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.MainActivity;
import com.iesgala.qremember.activities.NuevoLugarActivity;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class NuevoLugarController {

    public static void nuevoLugar(Activity activity, String longitud, String latitud, String altitud, String enlace, String nombreLugar, String emailUsuario, byte[] bmData, ArrayList<String> nombresCategoria){
        try {
            new AsyncTasks.InsertTask().execute("INSERT INTO lugar (longitud, latitud, altitud, enlace, nombre) VALUES (" + longitud + ", " + latitud + ", " + altitud + ", '" + enlace + "', '" + nombreLugar + "')").get(1, TimeUnit.MINUTES);
            new AsyncTasks.InsertTask().execute("INSERT INTO `lugar_usuario`(`longitud`, `latitud`, `altitud`, `enlace`, `email_usuario`) VALUES ('"+longitud+"','"+latitud+"','"+altitud+"','" + enlace + "','"+emailUsuario+"')").get(1, TimeUnit.MINUTES);
            new AsyncTasks.PreparedInsertImageTask().execute(bmData,longitud,latitud,altitud,enlace).get(1,TimeUnit.MINUTES);
            for (String categoria : nombresCategoria) {
                new AsyncTasks.InsertTask().execute("INSERT INTO lugar_categoria (longitud, latitud, altitud,enlace, nombre_categoria) VALUES ('" + longitud + "','" + latitud + "', '" + altitud + "','" + enlace + "','" + categoria + "')").get(1, TimeUnit.MINUTES);
            }

        }catch (InterruptedException | ExecutionException | TimeoutException e){
            Utils.AlertDialogGenerate(activity,activity.getString(R.string.err),e.getMessage());
        }
    }
    public static void finalizar(Activity activity,String emailUsuario){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(Utils.INTENTS_EMAIL,emailUsuario);
        activity.setResult(NuevoLugarActivity.NUEVOLUGARACTIVITY_CODE,intent);
        activity.finish();
    }
}
