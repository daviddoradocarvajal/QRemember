package com.iesgala.qremember.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.StartActivityController;
import com.iesgala.qremember.utils.MySQLClient;

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
        MySQLClient sql = new MySQLClient();
        System.out.println(sql.getConn());
        sql.selectDePrueba();
        Intent intent = getIntent();
        if (intent != null){
            System.out.println(intent.getBooleanExtra("Resultado",false));
        }
        TextView tvUsuario = findViewById(R.id.tvUsuario);
        TextView tvContrasenia = findViewById(R.id.tvContrasenia);
        TextView tv = findViewById(R.id.tvPulsaAqui);
        // TODO declarar los eventos de los botones para enviar a otras actividades.
        tv.setOnClickListener(e -> System.out.println("He sido pulsado"));
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(l -> StartActivityController.accederButton(tvUsuario.getText().toString(),tvContrasenia.getText().toString(),this));
        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(l -> StartActivityController.registrarButton(this));
    }
}