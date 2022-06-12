package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.widget.Toast;

import com.iesgala.qremember.R;
import com.iesgala.qremember.utils.AsyncTasks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class CompartirLugarController {
    public static void compartir(Activity activity,String emailReceptor,String emailEmisor,String enlace,String longitud,String latitud,String altitud){
        try {
            ResultSet resultSetComprobacionYaRegistrado = new AsyncTasks.SelectTask().execute("SELECT * FROM lugar_usuario WHERE email_usuario = '"+emailReceptor+"' AND enlace='"+enlace+"'").get(1, TimeUnit.MINUTES);
            if(resultSetComprobacionYaRegistrado.next()){
                Toast.makeText(activity, activity.getString(R.string.msg_ya_registrado),Toast.LENGTH_LONG).show();
            }else {
                ResultSet resultSetComprobacionYaCompartido = new AsyncTasks.SelectTask().execute("SELECT * FROM `lugares_compartidos` WHERE enlace='"+enlace+"' AND usuario_receptor='"+emailReceptor+"'").get(1,TimeUnit.MINUTES);
                if (resultSetComprobacionYaCompartido.next()) {
                    Toast.makeText(activity, activity.getString(R.string.msg_ya_compartido),Toast.LENGTH_LONG).show();
                } else {
                    if (new AsyncTasks.InsertTask().execute("INSERT INTO lugares_compartidos (usuario_emisor, latitud, longitud, altitud, enlace, usuario_receptor) VALUES ('" + emailEmisor + "','" + latitud + "','" + longitud + "','" + altitud + "','" + enlace + "','" + emailReceptor + "')").get(1, TimeUnit.MINUTES)) {
                        Toast.makeText(activity, activity.getString(R.string.msg_compartido_ok),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity,activity.getString(R.string.err_desconocido),Toast.LENGTH_LONG).show();
                    }
                }
            }
            activity.finish();
        } catch (ExecutionException | InterruptedException | TimeoutException | SQLException e) {
            e.printStackTrace();
        }
    }
}
