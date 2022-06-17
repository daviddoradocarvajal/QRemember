package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.NuevaRutaActivity;
import com.iesgala.qremember.activities.PopupRutaActivity;
import com.iesgala.qremember.model.BasicLugar;
import com.iesgala.qremember.model.Categoria;
import com.iesgala.qremember.model.Ruta;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/**
 * Clase controladora de la actividad RutasActivity maneja los eventos para obtener las rutas
 * del usuario, filtrarlas, ver una ruta y crear una nueva ruta
 * @author David Dorado
 * @version 1.0
 */
public class RutasController {
    /**
     * Método para obtener todas las rutas del usuario
     * @param activity Activiad que ejecuta el método
     * @param emailUsuario Email del usuario del cual obtener las rutas
     * @return Una lista con las rutas del usuario
     */
    public static ArrayList<Ruta> obtenerRutas(Activity activity,String emailUsuario){
        ArrayList<Ruta> rutas = new ArrayList<>();
        try{
            ResultSet resultSetRuta = new AsyncTasks.SelectTask().execute("SELECT nombre,email_usuario FROM ruta where email_usuario='"+emailUsuario+"'").get(1, TimeUnit.MINUTES);
            if(resultSetRuta!=null) {
                while (resultSetRuta.next()){
                    ResultSet resultSetLugarRuta = new AsyncTasks.SelectTask().execute("SELECT l.longitud as longitud, l.latitud as latitud, l.altitud as altitud, l.enlace as enlace, l.nombre as nombre FROM lugar l INNER JOIN lugar_ruta r ON l.enlace = r.enlace WHERE r.email_ruta='"+resultSetRuta.getString("email_usuario")+"' AND r.nombre_ruta='"+resultSetRuta.getString("nombre")+"'").get(1,TimeUnit.MINUTES);
                    ArrayList<BasicLugar> lugares = new ArrayList<>();
                    if(resultSetLugarRuta!=null){
                        while (resultSetLugarRuta.next()){
                            lugares.add(new BasicLugar(Float.parseFloat(resultSetLugarRuta.getString("longitud")),Float.parseFloat(resultSetLugarRuta.getString("latitud")),Float.parseFloat(resultSetLugarRuta.getString("altitud")),resultSetLugarRuta.getString("enlace"),resultSetLugarRuta.getString("nombre")));
                        }
                        ResultSet resultsetRutaCategoria = new AsyncTasks.SelectTask().execute("SELECT nombre_categoria FROM ruta_categoria WHERE nombre_ruta='"+resultSetRuta.getString("nombre")+"' and email_ruta='"+emailUsuario+"'").get(1,TimeUnit.MINUTES);
                        ArrayList<Categoria> categorias = new ArrayList<>();
                        if(resultsetRutaCategoria!=null){
                            while (resultsetRutaCategoria.next()){
                                categorias.add(new Categoria(resultsetRutaCategoria.getString("nombre_categoria")));
                            }
                            rutas.add(new Ruta(
                               resultSetRuta.getString("nombre"),
                               resultSetRuta.getString("email_usuario"),
                               lugares,
                               categorias
                            ));
                        }
                    }
                }
            }
            return rutas;
        }catch (SQLException | ExecutionException | InterruptedException | TimeoutException e){
            Utils.AlertDialogGenerate(activity.getBaseContext(), activity.getString(R.string.err), e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método para obtener las rutas del usuario que pertenezcan a una categoria en concreto
     * @param activity Actividad que ejecuta el método
     * @param emailUsuario Email del usuario
     * @param categoria Categoria por la cual filtrar las rutas
     * @return Una lista con las rutas que pertenezcan a la categoria indicada como tercer parámetro
     */
    public static ArrayList<Ruta> obtenerRutasFiltradas(Activity activity,String emailUsuario, String categoria){
        ArrayList<Ruta> rutas = new ArrayList<>();
        try{
            ResultSet resultSetRuta = new AsyncTasks.SelectTask().execute("SELECT r.nombre as nombre,r.email_usuario as email_usuario FROM ruta r INNER JOIN ruta_categoria c where r.email_usuario='"+emailUsuario+"' AND c.nombre_categoria='"+categoria+"'").get(1, TimeUnit.MINUTES);
            if(resultSetRuta!=null) {
                while (resultSetRuta.next()){
                    ResultSet resultSetLugarRuta = new AsyncTasks.SelectTask().execute("SELECT l.longitud as longitud, l.latitud as latitud, l.altitud as altitud, l.enlace as enlace, l.nombre as nombre FROM lugar l INNER JOIN lugar_ruta r ON l.enlace = r.enlace WHERE r.email_ruta='"+resultSetRuta.getString("email_usuario")+"' AND r.nombre_ruta='"+resultSetRuta.getString("nombre")+"'").get(1,TimeUnit.MINUTES);
                    ArrayList<BasicLugar> lugares = new ArrayList<>();
                    if(resultSetLugarRuta!=null){
                        while (resultSetLugarRuta.next()){
                            lugares.add(new BasicLugar(Float.parseFloat(resultSetLugarRuta.getString("longitud")),Float.parseFloat(resultSetLugarRuta.getString("latitud")),Float.parseFloat(resultSetLugarRuta.getString("altitud")),resultSetLugarRuta.getString("enlace"),resultSetLugarRuta.getString("nombre")));
                        }
                        ResultSet resultsetRutaCategoria = new AsyncTasks.SelectTask().execute("SELECT nombre_categoria FROM ruta_categoria WHERE nombre_ruta='"+resultSetRuta.getString("nombre")+"' and email_ruta='"+emailUsuario+"'").get(1,TimeUnit.MINUTES);
                        ArrayList<Categoria> categorias = new ArrayList<>();
                        if(resultsetRutaCategoria!=null){
                            while (resultsetRutaCategoria.next()){
                                categorias.add(new Categoria(resultsetRutaCategoria.getString("nombre_categoria")));
                            }
                            rutas.add(new Ruta(
                                    resultSetRuta.getString("nombre"),
                                    resultSetRuta.getString("email_usuario"),
                                    lugares,
                                    categorias
                            ));
                        }
                    }
                }
            }
            return rutas;
        }catch (SQLException | ExecutionException | InterruptedException | TimeoutException e){
            Utils.AlertDialogGenerate(activity.getBaseContext(), activity.getString(R.string.err), e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método que responde al click del botón ver ruta, lanza un Intent a la clase PopupRutaActivity
     * con los datos de la ruta
     * @param activity Actividad que lanza el evento
     * @param nombreRuta Nombre de la ruta a mostrar
     * @param emailUsuario Email del usuario
     * @param lugares Lista con los lugares del usuario en un objeto serializable
     */
    public static void verRuta(Activity activity, String nombreRuta, String emailUsuario,ArrayList<BasicLugar> lugares) {
        Intent intent = new Intent(activity, PopupRutaActivity.class);
        // Si el objeto no implementa serializable no se puede enviar en un intent
        intent.putExtra(Utils.INTENTS_BASICLUGAR,lugares);
        intent.putExtra(Utils.INTENTS_NOMBRE_RUTA,nombreRuta);
        intent.putExtra(Utils.INTENTS_EMAIL,emailUsuario);
        activity.startActivity(intent);

    }

    /**
     * Método que lanza la actividad NuevaRutaActivity al pulsar sobre el botón Nueva en la lista
     * de rutas enviando el email del usuario en el Intent
     * @param activity Acitivdad que lanza el evento
     * @param emailUsuario Email del usuario
     */
    public static void nuevaRuta(Activity activity,String emailUsuario){
        Intent intent = new Intent(activity, NuevaRutaActivity.class);
        intent.putExtra(Utils.INTENTS_EMAIL,emailUsuario);
        activity.startActivity(intent);
    }
}
