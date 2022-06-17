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
 * Clase controladora de la actividad CompartidoConmigoActivity, maneja los eventos al iniciar la
 * actividad para obtener los lugares y las rutas compartidos con el usuario, mostrarlos en sus
 * respetivos adaptadores y de responder a los eventos de aceptar y rechazar tanto lugares como
 * rutas
 * @author David Dorado
 * @version 1.0
 */
public class CompartidoConmigoController {

    /**
     * Método que obtiene las rutas compartidas con el usuario y las devuelve en una lista
     * @param emailReceptor cadena de caracteres con el email del usuario
     * @return Una lista con las rutas compartidas con el usuario
     */
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
    /**
     * Método que obtiene los lugares compartidos con el usuario y los devuelve en una lista
     * @param emailReceptor cadena de caracteres con el email del usuario
     * @return Una lista con los lugares compartidos con el usuario
     */
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

    /**
     * Método que responde al click sobre el botón aceptar en la lista de lugares compartidos
     * se encarga de insertar el lugar en la base de datos y eliminar de la lista de lugares
     * compartidos el elemento
     * @param activity Actividad que lanza el evento
     * @param lugarCompartido Objeto con los datos del lugar a aceptar
     */
    public static void aceptarCompartido(Activity activity, LugarCompartido lugarCompartido) {
        try {
            // Se inserta en lugar_usuario los datos del lugar junto con el email del receptor
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

    /**
     * Método que responde al click sobre el botón rechazar en la lista de lugares compartidos,
     * elimina el registo de la base de datos y actualiza la lista para que no aparezca el lugar
     * rechazado
     * @param activity Actividad que lanza el evento
     * @param lugarCompartido Objeto con los datos del lugar a rechazar
     */
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

    /**
     * Método encargado de actualizar el adaptador de ListView de los lugares compartidos con el
     * usuario
     * @param activity Activity donde se ejecuta el método
     * @param emailReceptor email del usuario logueado en la aplicación del cual comprobar sus
     *                      lugares compartidos
     */
    public static void setLugaresAdapter(Activity activity, String emailReceptor) {
        // Se obtiene una lista con los lugares
        ArrayList<LugarCompartido> compartidos = CompartidoConmigoController.obtenerLugaresCompartidos(emailReceptor);
        // Se inicializa el adaptador
        LugaresCompartidosAdapter lugaresCompartidosAdapter = new LugaresCompartidosAdapter(activity, compartidos);
        // Se obtiene la referencia del ListView
        ListView lvCompartidos = activity.findViewById(R.id.lvLugaresCompartidos);
        // Se le asigna el adaptador al ListView
        lvCompartidos.setAdapter(lugaresCompartidosAdapter);
    }

    /**
     * Método encargado de actualizar el adaptador de ListView de las rutas compartidas con el
     * usuario
     * @param activity Activity donde se ejecuta el método
     * @param emailReceptor email del usuario logueado en la aplicación del cual comprobar sus
     *                      rutas compartidas
     */
    public static void setRutasAdapter(Activity activity, String emailReceptor) {
        ArrayList<RutaCompartida> rutasCompartidas = CompartidoConmigoController.obtenerRutasCompartidas(emailReceptor);
        RutasCompartidasAdapter rutasCompartidasAdapter = new RutasCompartidasAdapter(activity, rutasCompartidas);
        ListView lvRutasCompartidas = activity.findViewById(R.id.lvRutasCompartidas);
        lvRutasCompartidas.setAdapter(rutasCompartidasAdapter);
    }
    /**
     * Método que responde al click sobre el botón aceptar en la lista de rutas compartidas
     * @param activity Actividad que lanza el evento
     * @param rutaCompartida Objeto con los datos de la ruta a compartir
     */
    public static void aceptarRutaCompartida(Activity activity, RutaCompartida rutaCompartida) {
        try {
            //Se inserta en la tabla ruta la ruta con el email del usuario
            if (new AsyncTasks.InsertTask().execute("INSERT INTO ruta(nombre,email_usuario) VALUES ('" + rutaCompartida.getNombreRuta() + "','" + rutaCompartida.getEmailReceptor() + "')").get(1, TimeUnit.MINUTES)) {
                // Se buscan las categorias de la ruta
                ResultSet resultSetRutaCategoria = new AsyncTasks.SelectTask().execute("SELECT nombre_categoria FROM ruta_categoria WHERE nombre_ruta='" + rutaCompartida.getNombreRuta() + "' AND email_ruta = '" + rutaCompartida.getEmailEmisor() + "'").get(1, TimeUnit.MINUTES);
                if (resultSetRutaCategoria != null) {
                    while (resultSetRutaCategoria.next()) {
                        // Se insertan en las categorias de la ruta la ruta con el email del usuario receptor
                        if (new AsyncTasks.InsertTask().execute("INSERT INTO ruta_categoria(nombre_ruta, email_ruta, nombre_categoria) VALUES ('" + rutaCompartida.getNombreRuta() + "','" + rutaCompartida.getEmailReceptor() + "','" + resultSetRutaCategoria.getString("nombre_categoria") + "')").get(1, TimeUnit.MINUTES)) {

                        } else
                            Toast.makeText(activity, activity.getString(R.string.err_desconocido), Toast.LENGTH_LONG);
                    }
                    // Se obtienen los lugares de la ruta
                    ResultSet resultSetLugarRuta = new AsyncTasks.SelectTask().execute("SELECT longitud, latitud, altitud, enlace, nombre_ruta, email_ruta FROM lugar_ruta WHERE nombre_ruta='" + rutaCompartida.getNombreRuta() + "' AND email_ruta='"+rutaCompartida.getEmailEmisor()+"'").get(1, TimeUnit.MINUTES);
                    if (resultSetLugarRuta != null) {
                        while (resultSetLugarRuta.next()) {
                            // Se insertan registros para los lugares de la ruta con el email del usuario receptor
                            new AsyncTasks.InsertTask().execute("INSERT INTO lugar_ruta(longitud, latitud, altitud, enlace, nombre_ruta, email_ruta) VALUES ('" + resultSetLugarRuta.getString("longitud") + "','" + resultSetLugarRuta.getString("latitud") + "','" + resultSetLugarRuta.getString("altitud") + "','" + resultSetLugarRuta.getString("enlace") + "','" + rutaCompartida.getNombreRuta() + "','" + rutaCompartida.getEmailReceptor() + "')").get(1, TimeUnit.MINUTES);
                        }
                        // Finalmente se elimina el registro de la tabla rutas_compartidas y se actualiza el adaptador
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
    /**
     * Método que responde al click sobre el botón rechazar en la lista de rutas compartidas,
     * elimina el registo de la base de datos y actualiza la lista para que no aparezca la ruta
     * rechazada
     * @param activity Actividad que lanza el evento
     * @param rutaCompartida Objeto con los datos de la ruta a rechazar
     */
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
