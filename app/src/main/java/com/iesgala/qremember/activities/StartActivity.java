package com.iesgala.qremember.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.StartActivityController;
/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        TextView tvUsuario = findViewById(R.id.tvUsuario);
        TextView tvContrasenia = findViewById(R.id.tvContrasenia);
        TextView tv = findViewById(R.id.tvPulsaAqui);
        // TODO declarar los eventos de los botones para enviar a otras actividades.
        tv.setOnClickListener(e -> System.out.println("He sido pulsado"));

        Button btn = findViewById(R.id.btnLogin);
        btn.setOnClickListener(l -> StartActivityController.accederButton(tvUsuario.getText().toString(),tvContrasenia.getText().toString(),this));
    }
}