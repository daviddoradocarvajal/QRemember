package com.iesgala.qremember.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.MainActivityController;
import com.iesgala.qremember.controllers.StartActivityController;
import com.iesgala.qremember.model.Usuario;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start);
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.bienvenida);
            TextView tvUsuario = findViewById(R.id.tvUsuario);
            TextView tvRecuperarPass = findViewById(R.id.tvPulsaAqui);
            tvRecuperarPass.setOnClickListener(e -> StartActivityController.recuperarContrasenia(this));
            Button btnLogin = findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(l -> StartActivityController.accederButton(this));
            Button btnRegistrar = findViewById(R.id.btnRegistrar);
            btnRegistrar.setOnClickListener(l -> StartActivityController.registrarButton(this));
        }catch (Exception e){
            Utils.AlertDialogGenerate(this,getString(R.string.err),e.getMessage());
        }

    }

}

