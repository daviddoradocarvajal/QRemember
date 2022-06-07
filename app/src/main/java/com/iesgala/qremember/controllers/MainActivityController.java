package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;

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

    public static void verEnlace(Uri uri,Activity activity){
        Intent navegador = new Intent(Intent.ACTION_VIEW,uri);
        activity.startActivity(navegador);

    }

    public static void nuevoLugar(Activity activity){
        leerQr(activity);

    }
    private static void leerQr(Activity activity){
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
    }
}
