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
/**
 * Clase controladora de la actividad NuevaRutaActivity maneja el evento para crear una nueva ruta
 * @author David Dorado
 * @version 1.0
 */
public class NuevaRutaController {
    /**
     * Método que responde al evento para crear una nueva ruta, realiza las operaciones necesarias
     * para insertar una nueva ruta en la base de datos
     * @param activity Actividad que lanza el evento
     * @param email email del usuario
     * @param nombreRuta nombre de la ruta a almacenar
     * @param lugaresInsertar lista con los lugares de la ruta a almacenar
     * @param categoriasSeleccionadas lista con las categorias de la ruta a almacenar
     */
    public static void nuevaRuta(Activity activity, String email, String nombreRuta, ArrayList<Lugar> lugaresInsertar, ArrayList<String> categoriasSeleccionadas){
        try{
            // Se comprueba que no haya una ruta ya con el mismo nombre
            ResultSet resultSetRutasUsuario = new AsyncTasks.SelectTask().execute("SELECT nombre FROM ruta WHERE nombre='"+nombreRuta+"' AND email_usuario='"+email+"'").get(1,TimeUnit.MINUTES);
            if(!resultSetRutasUsuario.next()){
                // Se inserta la ruta en la base de datos
                if(new AsyncTasks.InsertTask().execute("INSERT INTO ruta(nombre, email_usuario) VALUES ('"+nombreRuta+"','"+email+"')").get(1, TimeUnit.MINUTES)){
                    // Por cada uno de los lugares se inserta una nueva fila relacionando el lugar con la ruta
                    for(Lugar l:lugaresInsertar){
                        new AsyncTasks.InsertTask().execute("INSERT INTO lugar_ruta(longitud, latitud, altitud, enlace, nombre_ruta, email_ruta) VALUES ('"+l.getLongitud()+"','"+l.getLatitud()+"','"+l.getAltitud()+"','"+l.getEnlace()+"','"+nombreRuta+"','"+email+"')").get(1,TimeUnit.MINUTES);
                    }
                    for(String c:categoriasSeleccionadas){
                        // Por cada categoria se inserta en la base de datos la relación de ruta con categoria
                        new AsyncTasks.InsertTask().execute("INSERT INTO ruta_categoria(nombre_ruta, email_ruta, nombre_categoria) VALUES ('"+nombreRuta+"','"+email+"','"+c+"')").get(1,TimeUnit.MINUTES);
                    }
                }
                /*
                 * Si termina correctamente se lanza un intent con el email del usuario a fin de
                 * no perder los datos de sesión y se finaliza la actividad que lanzó el evento
                 */
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
