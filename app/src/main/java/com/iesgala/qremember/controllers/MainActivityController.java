package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;

import com.iesgala.qremember.activities.PopupLugarActivity;
import com.iesgala.qremember.model.Lugar;


/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class MainActivityController {

    public static void clickLugar(Activity activity, Lugar lugar){
        Intent intent = new Intent(activity.getBaseContext(), PopupLugarActivity.class);

        activity.startActivity(intent);
    }

    public static void verEnlace(){

    }

    public static void nuevoLugar(){
        //IntentI
        // Lanzar camara
        // Obtener resultado camara
        // Leer qr
        // Sacar URL del qr
        // Obtener localizacion
        // Enviar al intent qr y localizacion
        // Lanzar activity nuevo lugar

    }
}
