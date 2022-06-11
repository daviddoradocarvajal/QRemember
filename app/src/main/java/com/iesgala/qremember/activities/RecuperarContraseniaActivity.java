package com.iesgala.qremember.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.RecuperarContraseniaController;
import com.iesgala.qremember.model.Usuario;
import com.iesgala.qremember.utils.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * @author David Dorado
 * @version 1.0
 */
public class RecuperarContraseniaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperarcontrasenia);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Recuperar contraseña");

        Spinner spPreguntasRecuperar = findViewById(R.id.spPreguntaRecuperar);
        ArrayAdapter<CharSequence> spPreguntasAdapter = ArrayAdapter.createFromResource(this,R.array.Preguntas, android.R.layout.simple_spinner_item);
        spPreguntasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPreguntasRecuperar.setAdapter(spPreguntasAdapter);

        Button btnComprobar = findViewById(R.id.btnComprobar);
        btnComprobar.setOnClickListener(l -> RecuperarContraseniaController.recuperarContrasenia(this));
    }

}

