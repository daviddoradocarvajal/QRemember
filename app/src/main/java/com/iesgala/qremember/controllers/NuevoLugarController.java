package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.MainActivity;
import com.iesgala.qremember.activities.NuevoLugarActivity;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static int nuevoLugar(Activity activity, String longitud, String latitud, String altitud, String enlace, String nombreLugar, String emailUsuario, byte[] bmData, ArrayList<String> nombresCategoria){
        try {
            ResultSet resultSetLugares = new AsyncTasks.SelectTask().execute("SELECT longitud, latitud, altitud,enlace from lugar WHERE longitud = '"+longitud+"' AND latitud = '"+latitud+"' AND altitud = '"+altitud+"' AND enlace = '"+enlace+"'").get(1,TimeUnit.MINUTES);
            if(resultSetLugares.next()){
                ResultSet resultSetLugarUsuario = new AsyncTasks.SelectTask().execute("SELECT longitud, latitud, altitud, enlace, email_usuario FROM lugar_usuario WHERE longitud = '"+longitud+"' AND latitud = '"+latitud+"' AND altitud = '"+altitud+"' AND enlace = '"+enlace+"' AND email_usuario = '"+emailUsuario+"'").get(1,TimeUnit.MINUTES);
                if(resultSetLugarUsuario.next()){
                    return 1;
                }else {
                    new AsyncTasks.InsertTask().execute("INSERT INTO lugar_usuario (longitud, latitud, altitud, enlace, email_usuario) VALUES ('" + longitud + "','" + latitud + "','" + altitud + "','" + enlace + "','" + emailUsuario + "')").get(1, TimeUnit.MINUTES);
                    new AsyncTasks.PreparedInsertImageTask().execute(bmData, longitud, latitud, altitud, enlace).get(1, TimeUnit.MINUTES);
                    return 2;
                }
            }else {
                new AsyncTasks.InsertTask().execute("INSERT INTO lugar (longitud, latitud, altitud, enlace, nombre) VALUES (" + longitud + ", " + latitud + ", " + altitud + ", '" + enlace + "', '" + nombreLugar + "')").get(1, TimeUnit.MINUTES);
                new AsyncTasks.InsertTask().execute("INSERT INTO lugar_usuario (longitud, latitud, altitud, enlace, email_usuario) VALUES ('" + longitud + "','" + latitud + "','" + altitud + "','" + enlace + "','" + emailUsuario + "')").get(1, TimeUnit.MINUTES);
                new AsyncTasks.PreparedInsertImageTask().execute(bmData, longitud, latitud, altitud, enlace).get(1, TimeUnit.MINUTES);
                for (String categoria : nombresCategoria) {
                    new AsyncTasks.InsertTask().execute("INSERT INTO lugar_categoria (longitud, latitud, altitud,enlace, nombre_categoria) VALUES ('" + longitud + "','" + latitud + "', '" + altitud + "','" + enlace + "','" + categoria + "')").get(1, TimeUnit.MINUTES);
                }
                return 0;
            }
        }catch (InterruptedException | ExecutionException | TimeoutException | SQLException e){
            Utils.AlertDialogGenerate(activity,activity.getString(R.string.err),e.getMessage());
            return -1;
        }
    }
    public static void finalizar(Activity activity,String emailUsuario, int respuesta){
        if(respuesta==1){
            Toast.makeText(activity, activity.getString(R.string.lugar_registrado_err),Toast.LENGTH_LONG).show();
        }else if(respuesta==2){
            Toast.makeText(activity, activity.getString(R.string.lugar_registrado_almacenado_usuario),Toast.LENGTH_LONG).show();
        }else if(respuesta==0){
            Toast.makeText(activity, activity.getString(R.string.lugar_registrado_ok),Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(Utils.INTENTS_EMAIL,emailUsuario);
        activity.setResult(NuevoLugarActivity.NUEVOLUGARACTIVITY_CODE,intent);
        activity.finish();
    }
}
