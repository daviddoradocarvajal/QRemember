package com.iesgala.qremember.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;

import androidx.core.app.ActivityCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.CaptureActivityPortrait;
import com.iesgala.qremember.activities.NuevoLugarActivity;
import com.iesgala.qremember.activities.PopupLugarActivity;
import com.iesgala.qremember.model.Categoria;
import com.iesgala.qremember.model.Imagen;
import com.iesgala.qremember.model.Lugar;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Clase controladora de la actividad MainActivity maneja los eventos para obtener los lugares de
 * un usuario, hacer click sobre un lugar, ver el enlace de un lugar, pulsar el boton de nuevo lugar
 * para leer el código QR del lugar y obtener los lugares filtrados por una categoria
 * @author David Dorado
 * @version 1.0
 */
public class MainActivityController {
    /**
     * Método que obtiene los lugares en la base de datos del email pasado como parámetro y los
     * devuelve en un objeto ArrayList
     * @param activity Actividad que ejecuta el método
     * @param emailUsuario cadena de caracteres con el correo electrónico del usuario
     * @return Una lista de objetos Lugar con los lugares del usuario
     */
    public static ArrayList<Lugar> obtenerLugares(Activity activity, String emailUsuario) {
        ArrayList<Lugar> lugares = new ArrayList<>();
        try {
            // Se obtienen los lugares del usuario
            String sql = "SELECT l.longitud,l.latitud,l.altitud,l.enlace,l.nombre FROM lugar l INNER JOIN lugar_usuario u ON l.enlace = u.enlace WHERE u.email_usuario = '" + emailUsuario + "'";
            ResultSet resultSetLugares = new AsyncTasks.SelectTask().execute(sql).get(1, TimeUnit.MINUTES);
            if (resultSetLugares != null) {
                while (resultSetLugares.next()) {
                    // Se obtienen las imagenes de cada lugar
                    ResultSet resultSetImagenes = new AsyncTasks.SelectTask().execute("SELECT id,imagen FROM imagen WHERE enlace = '" + resultSetLugares.getString("enlace") + "';").get(1, TimeUnit.MINUTES);
                    ArrayList<Imagen> imagenes = new ArrayList<>();
                    if (resultSetImagenes != null) {
                        while (resultSetImagenes.next()) {
                            // Por cada imagen se obtiene su inputStream con los bytes de la imagen
                            InputStream stream = resultSetImagenes.getBlob("imagen").getBinaryStream();
                            // Despues se agrega a un array de objetos imagen
                            imagenes.add(new Imagen(resultSetImagenes.getInt("id"), Drawable.createFromStream(stream, "imagen")));
                        }
                        // Se obtienen las categorias del lugar
                        ResultSet resultSetCategorias = new AsyncTasks.SelectTask().execute("SELECT nombre_categoria FROM lugar_categoria WHERE enlace = '" + resultSetLugares.getString("enlace") + "' ;").get(1, TimeUnit.MINUTES);
                        ArrayList<Categoria> categorias = new ArrayList<>();
                        if (resultSetCategorias != null) {
                            while (resultSetCategorias.next()) {
                                // Se añaden las categorias encontradas a una lista de objetos Categoria
                                categorias.add(new Categoria(resultSetCategorias.getString("nombre_categoria")));
                            }
                            /*
                             * Finalmente por cada lugar se añade a la lista de lugares un objeto lugar
                             * con la longitud, altitud, enlace, nombre del lugar y las listas con
                             * las imagenes y las categorias del lugar
                             */
                            lugares.add(
                                    new Lugar(
                                            resultSetLugares.getString("longitud"),
                                            resultSetLugares.getString("latitud"),
                                            resultSetLugares.getString("altitud"),
                                            resultSetLugares.getString("enlace"),
                                            resultSetLugares.getString("nombre"),
                                            imagenes,
                                            categorias
                                    ));
                        }
                    }
                }
            }
            // Una vez encontrados los lugares del usuario se devuelve la lista con los lugares
            return lugares;
        } catch (ExecutionException | InterruptedException | TimeoutException | SQLException e) {
            Utils.AlertDialogGenerate(activity.getBaseContext(), activity.getString(R.string.err), e.getMessage());
            return null;
        }
    }

    /**
     * Método que responde al evento de pulsación sobre un lugar para mostrar su ventana
     * con las imágenes. Recibe como parámetros la actividad que lanza el método y los datos del
     * lugar para enviarlos a traves de un intent a la Actividad PopupLugarActivity
     * @param activity Actividad que lanza el evento
     * @param position posición en la lista del lugar
     * @param enlace cadena con el enlace del código QR del lugar
     * @param email email del usuario
     * @param longitud Longitud del lugar para las coordenadas
     * @param latitud Latitud del lugar para las coordenadas
     * @param altitud Altitud del lugar para las coordenadas
     * @param nombreLugar nombre con el cual el usuario almacenó el lugar
     */
    public static void clickLugar(Activity activity, int position, String enlace, String email, String longitud, String latitud, String altitud,String nombreLugar) {
        Intent intent = new Intent(activity.getBaseContext(), PopupLugarActivity.class);
        intent.putExtra(Utils.INTENTS_EMAIL, email);
        intent.putExtra(Utils.INTENTS_ENLACE, enlace);
        intent.putExtra(Utils.INTENTS_POSICION, position);
        intent.putExtra(Utils.INTENTS_LONGITUD, longitud);
        intent.putExtra(Utils.INTENTS_LATITUD, latitud);
        intent.putExtra(Utils.INTENTS_ALTITUD, altitud);
        intent.putExtra(Utils.INTENTS_NOMBRE_LUGAR,nombreLugar);
        activity.startActivity(intent);
    }

    /**
     * Método encargado de abrir el navegador cuando se lanza el evento del botón ver enlace
     * @param uri Direccion web para abrir en el navegador
     * @param activity Actividad que lanza el evento
     */
    public static void verEnlace(Uri uri, Activity activity) {
        Intent navegador = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(navegador);
    }

    /**
     * Método que responde al el evento de click sobre el botón "Nuevo..." para añadir un nuevo lugar
     * comprueba si se tienen los permisos de ubicación y los solicita en caso de no tenerlos
     * @param activity Actividad que lanza el evento
     */
    public static void nuevoLugar(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        }
        leerQr(activity);

    }

    /**
     * Método que recibe una cadena con el enlace del código QR los datos de localización del
     * dispositivo y el email del usuario para enviarlos a la actividad NuevoLugarActivity con el
     * formulario para ponerle nombre y categorias al lugar
     * @param loc Objeto Location con los datos actuales o mas recientes de la localización del
     *            dispositivo
     * @param qrResult Cadena de caracteres con el enlace del código QR escaneado
     * @param emailUsuario Email del usuario
     * @param activity Actividad donde se ejecuta el método
     */
    public static void formularioNuevoLugar(Location loc, String qrResult, String emailUsuario, Activity activity) {
        Intent intent = new Intent(activity, NuevoLugarActivity.class);
        intent.putExtra(Utils.INTENTS_LONGITUD, String.valueOf(loc.getLongitude()));
        intent.putExtra(Utils.INTENTS_LATITUD, String.valueOf(loc.getLatitude()));
        intent.putExtra(Utils.INTENTS_ALTITUD, String.valueOf(loc.getAltitude()));
        intent.putExtra(Utils.INTENTS_ENLACE, qrResult);
        intent.putExtra(Utils.INTENTS_EMAIL, emailUsuario);
        activity.startActivity(intent);

    }

    /**
     * Método que lanza un intent para leer un código QR, si no se tienen permisos pide los permisos
     * para usar la cámara
     * @param activity Actividad sobre la que lanzar el evento
     */
    private static void leerQr(Activity activity) {
        //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Leer QR");
        integrator.setCameraId(0);
        integrator.setOrientationLocked(true);
        integrator.setTorchEnabled(false);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.initiateScan();
    }

    /**
     * Método que filtra la lista de lugares del usuario y devuelve la lista pero solo con los lugares
     * que pertenecen a la categoria introducida como tercer parámetro
     * @param activity Actividad que lanza el evento
     * @param emailUsuario Email del usuario
     * @param categoria Categoria por la que filtrar los lugares
     * @return Una lista con los lugares que pertenecen a la categoría indicada
     */
    public static ArrayList<Lugar> obtenerLugaresFiltrados(Activity activity, String emailUsuario,String categoria) {
        ArrayList<Lugar> lugares = new ArrayList<>();
        try {
            String sql = "SELECT l.longitud,l.latitud,l.altitud,l.enlace,l.nombre FROM lugar l INNER JOIN lugar_usuario u ON l.enlace = u.enlace INNER JOIN lugar_categoria c ON l.enlace = c.enlace WHERE u.email_usuario = '" + emailUsuario + "' AND c.nombre_categoria = '"+categoria+"'";
            ResultSet resultSetLugares = new AsyncTasks.SelectTask().execute(sql).get(1, TimeUnit.MINUTES);
            if (resultSetLugares != null) {
                while (resultSetLugares.next()) {
                    ResultSet resultSetImagenes = new AsyncTasks.SelectTask().execute("SELECT id,imagen FROM imagen WHERE enlace = '" + resultSetLugares.getString("enlace") + "';").get(1, TimeUnit.MINUTES);
                    ArrayList<Imagen> imagenes = new ArrayList<>();
                    if (resultSetImagenes != null) {
                        while (resultSetImagenes.next()) {
                            InputStream stream = resultSetImagenes.getBlob("imagen").getBinaryStream();
                            imagenes.add(new Imagen(resultSetImagenes.getInt("id"), Drawable.createFromStream(stream, "imagen")));
                        }
                        ResultSet resultSetCategorias = new AsyncTasks.SelectTask().execute("SELECT nombre_categoria FROM lugar_categoria WHERE enlace = '" + resultSetLugares.getString("enlace") + "' ;").get(1, TimeUnit.MINUTES);
                        ArrayList<Categoria> categorias = new ArrayList<>();
                        if (resultSetCategorias != null) {
                            while (resultSetCategorias.next()) {
                                categorias.add(new Categoria(resultSetCategorias.getString("nombre_categoria")));
                            }
                            lugares.add(
                                    new Lugar(
                                            resultSetLugares.getString("longitud"),
                                            resultSetLugares.getString("latitud"),
                                            resultSetLugares.getString("altitud"),
                                            resultSetLugares.getString("enlace"),
                                            resultSetLugares.getString("nombre"),
                                            imagenes,
                                            categorias
                                    ));
                        }
                    }
                }
            }
            return lugares;
        } catch (ExecutionException | InterruptedException | TimeoutException | SQLException e) {
            Utils.AlertDialogGenerate(activity.getBaseContext(), activity.getString(R.string.err), e.getMessage());
            return null;
        }
    }
}
