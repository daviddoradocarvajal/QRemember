package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.widget.Toast;

import com.iesgala.qremember.R;
import com.iesgala.qremember.utils.AsyncTasks;

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
public class ModificarLugarController {

    public static void modificarLugar(Activity activity, ArrayList<String> seleccionadas, String nombreLugar, String enlace, String longitud, String latitud, String altitud){
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
            activity.finish();
        }catch (InterruptedException | ExecutionException | TimeoutException e){
                e.printStackTrace();
        }
    }
}
