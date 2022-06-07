package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;

import com.iesgala.qremember.activities.NuevaContraseniaActivity;
import com.iesgala.qremember.activities.RegisterActivity;

/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class RecuperarContraseniaController {

    public static void recuperarContrasenia(String email, Activity activity){
        Intent intent = new Intent(activity.getBaseContext(), NuevaContraseniaActivity.class);
        intent.putExtra("Usuario",email);
        activity.startActivity(intent);
    }
}
