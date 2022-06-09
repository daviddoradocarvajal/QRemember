package com.iesgala.qremember.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.iesgala.qremember.R;

/**
 * Clase con las configuraciones de la aplicacion y métodos útiles para usar en várias clases
 *
 * @author David Dorado
 * @version 1.0
 */
public class Utils {
    public static final String SERVIDOR = "sql4.freemysqlhosting.net";
    public static final String PUERTO = "3306";
    public static final String BD = "sql4498629";
    public static final String USUARIO = "sql4498629";
    public static final String PASSWORD = "gkkIrdR3DJ";

    public static boolean createMenu(Menu menu, Activity activity) {
        activity.getMenuInflater().inflate(R.menu.menu_opciones, menu);
        MenuItem opcmenu1 = menu.findItem(R.id.miLocales);
        MenuItem opcmenu2 = menu.findItem(R.id.miRutas);
        MenuItem opcmenu3 = menu.findItem(R.id.miCompartido);
        MenuItem opcmenu4 = menu.findItem(R.id.miEliminar);
        MenuItem opcmenu5 = menu.findItem(R.id.miSalir);
        return true;
    }

    public static Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

}
