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
 * Clase controladora de la actividad NuevoLugarActivity maneja el evento de almacenar un nuevo
 * lugar en la base de datos para el usuario
 * @author David Dorado
 * @version 1.0
 */
public class NuevoLugarController {
    /**
     * Método que responde a la petición de registrar un nuevo lugar en la cuenta del usuario
     * logueado cuando se lanza el evento.
     * @param activity Actividad que lanzó el evento
     * @param longitud Longitud del lugar para las coordenadas
     * @param latitud Latitud del lugar para las coordenadas
     * @param altitud Altitud del lugar para las coordenadas
     * @param enlace Cadena de caracteres con el enlace almacenado en el código QR del lugar
     * @param nombreLugar Nombre del lugar a registar
     * @param emailUsuario Email del usuario
     * @param bmData Array de byte con los datos de la imagen a almacenar como foto principal del lugar
     * @param nombresCategoria Lista con las categorias en las que va a ser almacenado el lugar
     * @return -1 en caso una excepción
     *          1 Si el lugar ya está registrado en la cuenta del usuario
     *          2 Si el lugar estaba en la base de datos pero no en la cuenta del usuario
     *          0 Si el lugar se ha registrado como nuevo lugar en la base de datos y en la cuenta del
     *            usuario
     */
    public static int nuevoLugar(Activity activity, String longitud, String latitud, String altitud, String enlace, String nombreLugar, String emailUsuario, byte[] bmData, ArrayList<String> nombresCategoria){
        try {
            // Se comprueba si el lugar existe en la base de datos
            ResultSet resultSetLugares = new AsyncTasks.SelectTask().execute("SELECT longitud, latitud, altitud,enlace from lugar WHERE longitud = '"+longitud+"' AND latitud = '"+latitud+"' AND altitud = '"+altitud+"' AND enlace = '"+enlace+"'").get(1,TimeUnit.MINUTES);
            if(resultSetLugares.next()){
                // Si existe se comprueba si existe entre los lugares del usuario
                ResultSet resultSetLugarUsuario = new AsyncTasks.SelectTask().execute("SELECT longitud, latitud, altitud, enlace, email_usuario FROM lugar_usuario WHERE longitud = '"+longitud+"' AND latitud = '"+latitud+"' AND altitud = '"+altitud+"' AND enlace = '"+enlace+"' AND email_usuario = '"+emailUsuario+"'").get(1,TimeUnit.MINUTES);
                if(resultSetLugarUsuario.next()){
                    return 1;
                }else {
                    /*
                     * Si existe en la base de datos pero no en los lugares del usuario, se almacena
                     * en la cuenta del usuario y se añade la imagen tomada por el usuario a las
                     * imágenes del lugar
                     */
                    new AsyncTasks.InsertTask().execute("INSERT INTO lugar_usuario (longitud, latitud, altitud, enlace, email_usuario) VALUES ('" + longitud + "','" + latitud + "','" + altitud + "','" + enlace + "','" + emailUsuario + "')").get(1, TimeUnit.MINUTES);
                    new AsyncTasks.PreparedInsertImageTask().execute(bmData, longitud, latitud, altitud, enlace).get(1, TimeUnit.MINUTES);
                    return 2;
                }
            }else {
                /*
                 * Si el lugar no estaba en la base de datos se almacena como lugar nuevo, después
                 * se almacena en la cuenta del usuario y finalmente se almacena la imagen y las
                 * categorias
                 */
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

    /**
     * Método que se ejecuta una vez ejecutado el intento de registro del nuevo lugar,
     * para informar al usuario del resultado del registro y lanzar el intent para volver a la lista
     * de lugares
     * @param activity Actividad que ejecuta el método
     * @param emailUsuario email del usuario
     * @param respuesta entero con el código de respuesta del método nuevoLugar
     */
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
