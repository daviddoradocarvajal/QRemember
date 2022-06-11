package com.iesgala.qremember.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class PopupLugarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_lugar);
        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);
        int ancho = medidasVentana.widthPixels;
        int alto = medidasVentana.heightPixels;
        getSupportActionBar().setTitle("Aqui va el nombre del lugar");
        getWindow().setLayout((int)(ancho*0.95),(int)(alto * 0.95));
    }
}