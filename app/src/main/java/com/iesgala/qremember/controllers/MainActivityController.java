package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import com.google.zxing.integration.android.IntentIntegrator;
import com.iesgala.qremember.activities.CaptureActivityPortrait;
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

    public static void nuevoLugar(Activity activity){
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
