package com.iesgala.qremember.utils;

import android.annotation.SuppressLint;
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
import com.iesgala.qremember.activities.RutasActivity;
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
    public static final String ENCRYPT_PASS = "hunter1";
    public static final String INTENTS_EMAIL = "email";
    public static final String INTENTS_EMAIL_EMISOR = "emailEmisor";
    public static final String INTENTS_LONGITUD = "longitud";
    public static final String INTENTS_LATITUD = "latitud";
    public static final String INTENTS_ALTITUD = "altitud";
    public static final String INTENTS_ENLACE = "enlace";
    public static final String INTENTS_POSICION = "posicion";
    public static final String INTENTS_NOMBRE_LUGAR = "nombreLugar";

    public static boolean createMenu(Menu menu, Activity activity) {
        activity.getMenuInflater().inflate(R.menu.menu_opciones, menu);
        MenuItem opcmenu1 = menu.findItem(R.id.miLocales);
        MenuItem opcmenu2 = menu.findItem(R.id.miRutas);
        MenuItem opcmenu3 = menu.findItem(R.id.miCompartido);
        MenuItem opcmenu4 = menu.findItem(R.id.miEliminar);
        MenuItem opcmenu5 = menu.findItem(R.id.miSalir);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public static void menuOption(Activity activity, MenuItem item, String emailUsuario, String title) {
        switch (item.getItemId()) {
            case R.id.miLocales:
                if (title.equals(activity.getString(R.string.lugares)))
                    break;
                else {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra(Utils.INTENTS_EMAIL, emailUsuario);
                    activity.startActivity(intent);
                    activity.finish();
                }
            case R.id.miRutas:
                if (title.equals(activity.getString(R.string.rutas)))
                break;
                else {
                    Intent intent = new Intent(activity, RutasActivity.class);
                    intent.putExtra(Utils.INTENTS_EMAIL, emailUsuario);
                    activity.startActivity(intent);
                    activity.finish();
                }
            case R.id.miCompartido:
                if (title.equals(activity.getString(R.string.conmigo)))
                    break;
                else {
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
                builder.setPositiveButton(activity.getString(R.string.confirmar), (dialog, which) -> activity.finishAffinity());
                builder.setNegativeButton(activity.getString(R.string.cancelar), (dialog, which) -> {
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case android.R.id.home:
                activity.onBackPressed();
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
