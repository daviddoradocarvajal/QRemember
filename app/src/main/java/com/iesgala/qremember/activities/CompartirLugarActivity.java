package com.iesgala.qremember.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.CompartirLugarController;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Clase que maneja la actividad para compartir un lugar con otro usuario introduciendo el email
 * en el campo de texto que contiene
 * @author David Dorado
 * @version 1.0
 */
public class CompartirLugarActivity extends AppCompatActivity {
    String emailEmisor, enlace, longitud, latitud, altitud;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compartirlugar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.compartir_lugar));
        Intent intent = getIntent();
        if (intent != null) {
            emailEmisor = intent.getStringExtra(Utils.INTENTS_EMAIL_EMISOR);
            enlace = intent.getStringExtra(Utils.INTENTS_ENLACE);
            longitud = intent.getStringExtra(Utils.INTENTS_LONGITUD);
            latitud = intent.getStringExtra(Utils.INTENTS_LATITUD);
            altitud = intent.getStringExtra(Utils.INTENTS_ALTITUD);
            TextView tvEmailReceptor = findViewById(R.id.tvEmailReceptor);
            Button btnCompartir = findViewById(R.id.btnCompartirLugar);
            btnCompartir.setOnClickListener(l -> {
                try {
                    ResultSet resultSetUsuarioExiste = new AsyncTasks.SelectTask().execute("SELECT * FROM usuario WHERE email='"+tvEmailReceptor.getText().toString()+"'").get(1, TimeUnit.MINUTES);
                    if (resultSetUsuarioExiste.next()) {
                        CompartirLugarController.compartir(
                                this,
                                tvEmailReceptor.getText().toString(),
                                emailEmisor,
                                enlace,
                                longitud,
                                latitud,
                                altitud
                        );
                    } else {
                        Utils.AlertDialogGenerate(this,getString(R.string.msg_aviso),getString(R.string.err_usuario));
                    }
                } catch (ExecutionException | InterruptedException | TimeoutException | SQLException e) {
                    Utils.AlertDialogGenerate(this,getString(R.string.err),e.getMessage());
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.err_desconocido), Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Utils.INTENTS_EMAIL,emailEmisor);
        setResult(PopupLugarActivity.POPUPLUGAR_ACTIVITY_CODE, intent);
        super.onBackPressed();
    }
}
