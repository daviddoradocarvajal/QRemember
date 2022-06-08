package com.iesgala.qremember.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;

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
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            leerQr(activity);
        }


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
