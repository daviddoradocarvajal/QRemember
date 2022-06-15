package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;

import com.iesgala.qremember.activities.CompartirRutaActivity;
import com.iesgala.qremember.activities.ModificarRutaActivity;
import com.iesgala.qremember.utils.Utils;

public class PopupRutaController {

    public static void modificarRuta(Activity activity, String emailUsuario, String nombreRuta) {
        Intent intent = new Intent(activity, ModificarRutaActivity.class);
        intent.putExtra(Utils.INTENTS_NOMBRE_RUTA,nombreRuta);
        intent.putExtra(Utils.INTENTS_EMAIL,emailUsuario);
        activity.startActivity(intent);
    }

    public static void eliminarRuta(Activity activity, String emailUsuario, String nombreRuta) {

    }

    public static void compartirRuta(Activity activity, String emailUsuario, String nombreRuta) {
        Intent intent = new Intent(activity, CompartirRutaActivity.class);
        intent.putExtra(Utils.INTENTS_NOMBRE_RUTA,nombreRuta);
        intent.putExtra(Utils.INTENTS_EMAIL,emailUsuario);
        activity.startActivity(intent);
    }
}
