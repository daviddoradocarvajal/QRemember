package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;

import com.iesgala.qremember.R;
import com.iesgala.qremember.model.Lugar;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NuevaRutaController {

    public static void nuevaRuta(Activity activity, String email, String nombreRuta, ArrayList<Lugar> lugaresInsertar, ArrayList<String> categoriasSeleccionadas){
        try{
            ResultSet resultSetRutasUsuario = new AsyncTasks.SelectTask().execute("SELECT nombre FROM ruta WHERE nombre='"+nombreRuta+"' AND email_usuario='"+email+"'").get(1,TimeUnit.MINUTES);
            if(!resultSetRutasUsuario.next()){
                if(new AsyncTasks.InsertTask().execute("INSERT INTO ruta(nombre, email_usuario) VALUES ('"+nombreRuta+"','"+email+"')").get(1, TimeUnit.MINUTES)){
                    for(Lugar l:lugaresInsertar){
                        new AsyncTasks.InsertTask().execute("INSERT INTO lugar_ruta(longitud, latitud, altitud, enlace, nombre_ruta, email_ruta) VALUES ('"+l.getLongitud()+"','"+l.getLatitud()+"','"+l.getAltitud()+"','"+l.getEnlace()+"','"+nombreRuta+"','"+email+"')").get(1,TimeUnit.MINUTES);
                    }
                    for(String c:categoriasSeleccionadas){
                        new AsyncTasks.InsertTask().execute("INSERT INTO ruta_categoria(nombre_ruta, email_ruta, nombre_categoria) VALUES ('"+nombreRuta+"','"+email+"','"+c+"')").get(1,TimeUnit.MINUTES);
                    }
                }
                Intent intent = new Intent();
                intent.putExtra(Utils.INTENTS_EMAIL,email);
                activity.finish();
            }else Utils.AlertDialogGenerate(activity,activity.getString(R.string.msg_aviso),activity.getString(R.string.nombre_repetido));
        }catch (SQLException | ExecutionException | InterruptedException | TimeoutException e){
            e.printStackTrace();
            Utils.AlertDialogGenerate(activity,activity.getString(R.string.err),e.getMessage());
        }

    }
}
