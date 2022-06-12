package com.iesgala.qremember.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.CompartidoConmigoActivity;
import com.iesgala.qremember.activities.EliminarCuentaActivity;
import com.iesgala.qremember.activities.MainActivity;
import com.iesgala.qremember.model.Imagen;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Clase con las configuraciones de la aplicacion y métodos útiles para usar en várias clases
 *
 * @author David Dorado
 * @version 1.0
 */
public class Utils {
    public static final String SERVIDOR = "remotemysql.com";
    public static final String PUERTO = "3306";
    public static final String BD = "9eDAI0bmOi";
    public static final String USUARIO = "9eDAI0bmOi";
    public static final String PASSWORD = "KHnfj1mvHO";
    public static final String INTENTS_EMAIL = "email";
    public static final String INTENTS_LONGITUD = "longitud";
    public static final String INTENTS_LATITUD = "latitud";
    public static final String INTENTS_ALTITUD = "altitud";
    public static final String INTENTS_ENLACE = "enlace";
    public static final String INTENTS_POSICION = "posicion";


    public static boolean createMenu(Menu menu, Activity activity) {
        activity.getMenuInflater().inflate(R.menu.menu_opciones, menu);
        // Volver a locales
        MenuItem opcmenu1 = menu.findItem(R.id.miLocales);
        // Rutas?
        MenuItem opcmenu2 = menu.findItem(R.id.miRutas);
        // Actividad compartido
        MenuItem opcmenu3 = menu.findItem(R.id.miCompartido);
        // Actividad eliminar
        MenuItem opcmenu4 = menu.findItem(R.id.miEliminar);
        // if dialog true
        // Cerrar aplicacion
        MenuItem opcmenu5 = menu.findItem(R.id.miSalir);
        return true;
    }

    public static void menuOption(Activity activity, MenuItem item, String emailUsuario) {
        switch (item.getItemId()) {
            case R.id.miLocales:
                if (activity.getActionBar().getTitle() == activity.getString(R.string.lugares))
                    break;
                else {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra(Utils.INTENTS_EMAIL, emailUsuario);
                    activity.startActivity(intent);
                    activity.finish();
                }
            case R.id.miRutas:
                break;
            case R.id.miCompartido:
                if (activity.getActionBar().getTitle() == activity.getString(R.string.conmigo))
                    break;
                else {
                    //usuario_emisor 	latitud 	longitud 	altitud 	enlace 	usuario_receptor
                    Intent intent = new Intent(activity, CompartidoConmigoActivity.class);
                    intent.putExtra(Utils.INTENTS_EMAIL, emailUsuario);
                    activity.startActivity(intent);
                    activity.finish();
                }
                break;
            case R.id.miEliminar:
                Intent intent = new Intent(activity, EliminarCuentaActivity.class);
                intent.putExtra(Utils.INTENTS_EMAIL, emailUsuario);
                activity.startActivity(intent);
                activity.finish();
                break;
            case R.id.miSalir:

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(activity.getString(R.string.msg_aviso));
                builder.setMessage(R.string.seguro_salir);
                builder.setPositiveButton(activity.getString(R.string.confirmar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                builder.setNegativeButton(activity.getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
    }

    public static Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public static void AlertDialogGenerate(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
