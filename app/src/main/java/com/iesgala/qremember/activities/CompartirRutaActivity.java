package com.iesgala.qremember.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.CompartirRutaController;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CompartirRutaActivity extends AppCompatActivity {
    String emailEmisor, nombreRuta;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compartir_ruta);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.act_compartir_ruta);
        Intent intent = getIntent();
        if (intent != null) {
            emailEmisor = intent.getStringExtra(Utils.INTENTS_EMAIL_EMISOR);
            nombreRuta = intent.getStringExtra(Utils.INTENTS_NOMBRE_RUTA);
            TextView tvEmailReceptorRuta = findViewById(R.id.tvEmailReceptorRuta);
            Button btnConfirmarCompartirRuta = findViewById(R.id.btnConfirmarCompartirRuta);
            btnConfirmarCompartirRuta.setOnClickListener(l -> {
                try {
                    ResultSet resultSetUsuarioExiste = new AsyncTasks.SelectTask().execute("SELECT * FROM usuario WHERE email='"+tvEmailReceptorRuta.getText().toString()+"'").get(1, TimeUnit.MINUTES);
                    if (resultSetUsuarioExiste.next()) {
                        CompartirRutaController.compartirRuta(this,emailEmisor,tvEmailReceptorRuta.getText().toString(),nombreRuta);
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
