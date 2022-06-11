package com.iesgala.qremember.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.NuevaContraseniaController;
import com.iesgala.qremember.utils.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * @author David Dorado
 * @version 1.0
 */
public class NuevaContraseniaActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevacontrasenia);
        String email = getIntent().getStringExtra("Usuario");
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.recuperando_contrasenia) + email);
        TextView tvInfoNuevaPass = findViewById(R.id.tvInfoNuevaPass);
        tvInfoNuevaPass.setText(getString(R.string.recuperando_contrasenia) + " " + email);
        Button btnConfirmarNuevaPass = findViewById(R.id.btnConfirmarNuevaPass);
        btnConfirmarNuevaPass.setOnClickListener(i -> NuevaContraseniaController.actualizarContrasenia(this, email));
    }

}
