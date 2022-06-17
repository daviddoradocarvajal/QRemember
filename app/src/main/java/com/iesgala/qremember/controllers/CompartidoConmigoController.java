package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;

import com.iesgala.qremember.R;
import com.iesgala.qremember.adapters.LugaresCompartidosAdapter;
import com.iesgala.qremember.adapters.RutasCompartidasAdapter;
import com.iesgala.qremember.model.LugarCompartido;
import com.iesgala.qremember.model.RutaCompartida;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author David Dorado
 * @version 1.0
 */
public class CompartidoConmigoController {

    public static ArrayList<RutaCompartida> obtenerRutasCompartidas(String emailReceptor) {
        ArrayList<RutaCompartida> rutasCompartidas = new ArrayList<>();
        try {
            ResultSet resultSet = new AsyncTasks.SelectTask().execute("SELECT usuario_emisor, nombre_ruta, email_ruta, usuario_receptor FROM rutas_compartidas WHERE usuario_receptor='" + emailReceptor + "'").get(1, TimeUnit.MINUTES);
            if (resultSet != null) {
                while (resultSet.next()) {
                    rutasCompartidas.add(new RutaCompartida(
                            resultSet.getString("usuario_emisor"),
                            resultSet.getString("usuario_receptor"),
                            resultSet.getString("nombre_ruta")
                    ));
                }
            }
        } catch (SQLException | ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        return rutasCompartidas;
    }

    public static ArrayList<LugarCompartido> obtenerLugaresCompartidos(String emailReceptor) {
        ArrayList<LugarCompartido> compartidos = new ArrayList<>();
        try {
            ResultSet resultSet = new AsyncTasks.SelectTask().execute("SELECT usuario_emisor, latitud, longitud, altitud,enlace, usuario_receptor FROM lugares_compartidos WHERE usuario_receptor='" + emailReceptor + "'").get(1, TimeUnit.MINUTES);
            if (resultSet != null) {
                while (resultSet.next()) {
                    compartidos.add(new LugarCompartido(
                            resultSet.getString("enlace"),
                            resultSet.getString("longitud"),
                            resultSet.getString("latitud"),
                            resultSet.getString("altitud"),
                            resultSet.getString("usuario_emisor"),
                            resultSet.getString("usuario_receptor")
                    ));
                }
            }
        } catch (SQLException | ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        return compartidos;
    }


    public static void aceptarCompartido(Activity activity, LugarCompartido lugarCompartido) {
        try {
            if (new AsyncTasks.InsertTask().execute("INSERT INTO lugar_usuario (longitud, latitud, altitud, enlace, email_usuario) VALUES ('" + lugarCompartido.getLongitud() + "','" + lugarCompartido.getLatitud() + "','" + lugarCompartido.getAltitud() + "','" + lugarCompartido.getEnlace() + "','" + lugarCompartido.getEmailReceptor() + "')").get(1, TimeUnit.MINUTES)) {
                if (new AsyncTasks.DeleteTask().execute("DELETE FROM lugares_compartidos WHERE usuario_emisor='" + lugarCompartido.getEmailEmisor() + "' AND longitud='" + lugarCompartido.getLongitud() + "' AND latitud='" + lugarCompartido.getLatitud() + "' AND altitud='" + lugarCompartido.getAltitud() + "' AND enlace='" + lugarCompartido.getEnlace() + "' AND usuario_receptor='" + lugarCompartido.getEmailReceptor() + "'").get(1, TimeUnit.MINUTES)) {
                    setLugaresAdapter(activity, lugarCompartido.getEmailReceptor());
                } else
                    Utils.AlertDialogGenerate(activity, activity.getString(R.string.err), activity.getString(R.string.err_desconocido));
            } else
                Utils.AlertDialogGenerate(activity, activity.getString(R.string.err), activity.getString(R.string.err_desconocido));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        setLugaresAdapter(activity, lugarCompartido.getEmailReceptor());
    }


    public static void rechazarCompartido(Activity activity, LugarCompartido lugarCompartido) {
        try {
            if (new AsyncTasks.DeleteTask().execute("DELETE FROM lugares_compartidos WHERE usuario_emisor='" + lugarCompartido.getEmailEmisor() + "' AND longitud='" + lugarCompartido.getLongitud() + "' AND latitud='" + lugarCompartido.getLatitud() + "' AND altitud='" + lugarCompartido.getAltitud() + "' AND enlace='" + lugarCompartido.getEnlace() + "' AND usuario_receptor='" + lugarCompartido.getEmailReceptor() + "'").get(1, TimeUnit.MINUTES)) {
                setLugaresAdapter(activity, lugarCompartido.getEmailReceptor());
            } else
                Utils.AlertDialogGenerate(activity, activity.getString(R.string.err), activity.getString(R.string.err_desconocido));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        setLugaresAdapter(activity, lugarCompartido.getEmailReceptor());
    }

    public static void setLugaresAdapter(Activity activity, String emailReceptor) {
        ArrayList<LugarCompartido> compartidos = CompartidoConmigoController.obtenerLugaresCompartidos(emailReceptor);
        LugaresCompartidosAdapter lugaresCompartidosAdapter = new LugaresCompartidosAdapter(activity, compartidos);
        ListView lvCompartidos = activity.findViewById(R.id.lvLugaresCompartidos);
        lvCompartidos.setAdapter(lugaresCompartidosAdapter);
    }

    public static void setRutasAdapter(Activity activity, String emailReceptor) {
        ArrayList<RutaCompartida> rutasCompartidas = CompartidoConmigoController.obtenerRutasCompartidas(emailReceptor);
        RutasCompartidasAdapter rutasCompartidasAdapter = new RutasCompartidasAdapter(activity, rutasCompartidas);
        ListView lvRutasCompartidas = activity.findViewById(R.id.lvRutasCompartidas);
        lvRutasCompartidas.setAdapter(rutasCompartidasAdapter);
    }

    public static void aceptarRutaCompartida(Activity activity, RutaCompartida rutaCompartida) {
        try {
            if (new AsyncTasks.InsertTask().execute("INSERT INTO ruta(nombre,email_usuario) VALUES ('" + rutaCompartida.getNombreRuta() + "','" + rutaCompartida.getEmailReceptor() + "')").get(1, TimeUnit.MINUTES)) {
                ResultSet resultSetRutaCategoria = new AsyncTasks.SelectTask().execute("SELECT nombre_categoria FROM ruta_categoria WHERE nombre_ruta='" + rutaCompartida.getNombreRuta() + "' AND email_ruta = '" + rutaCompartida.getEmailEmisor() + "'").get(1, TimeUnit.MINUTES);
                if (resultSetRutaCategoria != null) {
                    while (resultSetRutaCategoria.next()) {
                        if (new AsyncTasks.InsertTask().execute("INSERT INTO ruta_categoria(nombre_ruta, email_ruta, nombre_categoria) VALUES ('" + rutaCompartida.getNombreRuta() + "','" + rutaCompartida.getEmailReceptor() + "','" + resultSetRutaCategoria.getString("nombre_categoria") + "')").get(1, TimeUnit.MINUTES)) {

                        } else
                            Toast.makeText(activity, activity.getString(R.string.err_desconocido), Toast.LENGTH_LONG);
                    }
                    ResultSet resultSetLugarRuta = new AsyncTasks.SelectTask().execute("SELECT longitud, latitud, altitud, enlace, nombre_ruta, email_ruta FROM lugar_ruta WHERE nombre_ruta='" + rutaCompartida.getNombreRuta() + "' AND email_ruta='"+rutaCompartida.getEmailEmisor()+"'").get(1, TimeUnit.MINUTES);
                    if (resultSetLugarRuta != null) {
                        while (resultSetLugarRuta.next()) {
                            new AsyncTasks.InsertTask().execute("INSERT INTO lugar_ruta(longitud, latitud, altitud, enlace, nombre_ruta, email_ruta) VALUES ('" + resultSetLugarRuta.getString("longitud") + "','" + resultSetLugarRuta.getString("latitud") + "','" + resultSetLugarRuta.getString("altitud") + "','" + resultSetLugarRuta.getString("enlace") + "','" + rutaCompartida.getNombreRuta() + "','" + rutaCompartida.getEmailReceptor() + "')").get(1, TimeUnit.MINUTES);
                        }
                        new AsyncTasks.DeleteTask().execute("DELETE FROM rutas_compartidas WHERE usuario_emisor='" + rutaCompartida.getEmailEmisor() + "' AND nombre_ruta='" + rutaCompartida.getNombreRuta() + "' AND email_ruta='" + rutaCompartida.getEmailEmisor() + "' AND usuario_receptor='" + rutaCompartida.getEmailReceptor() + "'").get(1, TimeUnit.MINUTES);
                        setRutasAdapter(activity, rutaCompartida.getEmailReceptor());
                    } else
                        Utils.AlertDialogGenerate(activity, activity.getString(R.string.err), activity.getString(R.string.err_desconocido));
                } else
                    Utils.AlertDialogGenerate(activity, activity.getString(R.string.err), activity.getString(R.string.err_desconocido));
            }
        } catch (SQLException | ExecutionException | InterruptedException | TimeoutException e) {
            Utils.AlertDialogGenerate(activity, activity.getString(R.string.err), e.getMessage());
            e.printStackTrace();
        }
    }

    public static void rechazarRutaCompartida(Activity activity, RutaCompartida rutaCompartida) {
        try {
            if (new AsyncTasks.DeleteTask().execute("DELETE FROM rutas_compartidas WHERE usuario_emisor='" + rutaCompartida.getEmailEmisor() + "' AND nombre_ruta='" + rutaCompartida.getNombreRuta() + "' AND email_ruta='" + rutaCompartida.getEmailEmisor() + "' AND usuario_receptor='" + rutaCompartida.getEmailReceptor() + "'").get(1, TimeUnit.MINUTES)) {
                setRutasAdapter(activity, rutaCompartida.getEmailReceptor());
            } else
                Utils.AlertDialogGenerate(activity, activity.getString(R.string.err), activity.getString(R.string.err_desconocido));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        setRutasAdapter(activity, rutaCompartida.getEmailReceptor());
    }

}
