package com.iesgala.qremember.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class MainActivityController {

    public static ArrayList<Lugar> obtenerLugares(Activity activity, String emailUsuario) {
        ArrayList<Lugar> lugares = new ArrayList<>();
        try {
            String sql = "SELECT l.longitud,l.latitud,l.altitud,l.enlace,l.nombre FROM lugar l INNER JOIN lugar_usuario u ON l.enlace = u.enlace WHERE u.email_usuario = '" + emailUsuario + "'";
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

    public static void clickLugar(Activity activity, int position, String enlace, String email, String longitud, String latitud, String altitud) {
        Intent intent = new Intent(activity.getBaseContext(), PopupLugarActivity.class);
        intent.putExtra(Utils.INTENTS_EMAIL, email);
        intent.putExtra(Utils.INTENTS_ENLACE, enlace);
        intent.putExtra(Utils.INTENTS_POSICION, position);
        intent.putExtra(Utils.INTENTS_LONGITUD, longitud);
        intent.putExtra(Utils.INTENTS_LATITUD, latitud);
        intent.putExtra(Utils.INTENTS_ALTITUD, altitud);
        activity.startActivity(intent);
    }

    public static void verEnlace(Uri uri, Activity activity) {
        Intent navegador = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(navegador);
    }


    public static void nuevoLugar(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        }
        leerQr(activity);

    }

    public static void formularioNuevoLugar(Location loc, String qrResult, String
            emailUsuario, Activity activity) {
        Intent intent = new Intent(activity, NuevoLugarActivity.class);
        intent.putExtra(Utils.INTENTS_LONGITUD, String.valueOf(loc.getLongitude()));
        intent.putExtra(Utils.INTENTS_LATITUD, String.valueOf(loc.getLatitude()));
        intent.putExtra(Utils.INTENTS_ALTITUD, String.valueOf(loc.getAltitude()));
        intent.putExtra(Utils.INTENTS_ENLACE, qrResult);
        intent.putExtra(Utils.INTENTS_EMAIL, emailUsuario);
        activity.startActivity(intent);

    }

    private static void leerQr(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
