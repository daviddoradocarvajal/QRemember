package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.widget.ListView;

import com.iesgala.qremember.R;
import com.iesgala.qremember.adapters.CompartidosAdapter;
import com.iesgala.qremember.model.LugarUsuario;
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

    public static ArrayList<LugarUsuario> obtenerCompartidos(Activity activity, String emailReceptor) {
        ArrayList<LugarUsuario> compartidos = new ArrayList<>();
        try {
            ResultSet resultSet = new AsyncTasks.SelectTask().execute("SELECT usuario_emisor, latitud, longitud, altitud,enlace, usuario_receptor FROM lugares_compartidos WHERE usuario_receptor='" + emailReceptor + "'").get(1, TimeUnit.MINUTES);
            if (resultSet != null) {
                while (resultSet.next()) {
                    compartidos.add(new LugarUsuario(
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


    public static void aceptarCompartido(Activity activity, LugarUsuario lugarUsuario) {
        try {
            if (new AsyncTasks.InsertTask().execute("INSERT INTO lugar_usuario (longitud, latitud, altitud, enlace, email_usuario) VALUES ('"+lugarUsuario.getLongitud()+"','"+lugarUsuario.getLatitud()+"','"+lugarUsuario.getAltitud()+"','"+lugarUsuario.getEnlace()+"','"+lugarUsuario.getEmailReceptor()+"')").get(1, TimeUnit.MINUTES)) {
                if (new AsyncTasks.DeleteTask().execute("DELETE FROM lugares_compartidos WHERE usuario_emisor='" + lugarUsuario.getEmailEmisor() + "' AND longitud='" + lugarUsuario.getLongitud() + "' AND latitud='" + lugarUsuario.getLatitud() + "' AND altitud='" + lugarUsuario.getAltitud() + "' AND enlace='" + lugarUsuario.getEnlace() + "' AND usuario_receptor='" + lugarUsuario.getEmailReceptor() + "'").get(1, TimeUnit.MINUTES)) {
                    setAdapter(activity, lugarUsuario.getEmailReceptor());
                } else
                    Utils.AlertDialogGenerate(activity, activity.getString(R.string.err), activity.getString(R.string.err_desconocido));
            } else
                Utils.AlertDialogGenerate(activity, activity.getString(R.string.err), activity.getString(R.string.err_desconocido));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        setAdapter(activity, lugarUsuario.getEmailReceptor());
    }


    public static void rechazarCompartido(Activity activity, LugarUsuario lugarUsuario) {
        try {
            if (new AsyncTasks.DeleteTask().execute("DELETE FROM lugares_compartidos WHERE usuario_emisor='" + lugarUsuario.getEmailEmisor() + "' AND longitud='" + lugarUsuario.getLongitud() + "' AND latitud='" + lugarUsuario.getLatitud() + "' AND altitud='" + lugarUsuario.getAltitud() + "' AND enlace='" + lugarUsuario.getEnlace() + "' AND usuario_receptor='" + lugarUsuario.getEmailReceptor() + "'").get(1, TimeUnit.MINUTES)) {
                setAdapter(activity, lugarUsuario.getEmailReceptor());
            } else
                Utils.AlertDialogGenerate(activity, activity.getString(R.string.err), activity.getString(R.string.err_desconocido));
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        setAdapter(activity, lugarUsuario.getEmailReceptor());
    }

    public static void setAdapter(Activity activity, String emailReceptor) {
        ArrayList<LugarUsuario> compartidos = CompartidoConmigoController.obtenerCompartidos(activity, emailReceptor);
        if (compartidos != null) {
            CompartidosAdapter compartidosAdapter = new CompartidosAdapter(activity, compartidos);
            ListView lvCompartidos = activity.findViewById(R.id.lvCompartidos);
            lvCompartidos.setAdapter(compartidosAdapter);
        }
    }
}
