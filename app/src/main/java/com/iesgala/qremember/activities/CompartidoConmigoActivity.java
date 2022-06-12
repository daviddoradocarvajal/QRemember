package com.iesgala.qremember.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.adapters.CompartidosAdapter;
import com.iesgala.qremember.adapters.LocalesAdapter;
import com.iesgala.qremember.controllers.CompartidoConmigoController;
import com.iesgala.qremember.model.LugarUsuario;
import com.iesgala.qremember.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author David Dorado
 * @version 1.0
 */
public class CompartidoConmigoActivity extends AppCompatActivity {
    String emailReceptor;
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compartidoconmigo);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.conmigo);
        title = getSupportActionBar().getTitle().toString();
        try {
            Intent intent = getIntent();
            if (intent != null) {
                emailReceptor = intent.getStringExtra(Utils.INTENTS_EMAIL);
                CompartidoConmigoController.setAdapter(this,emailReceptor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Aceptar agrega al usuario y elimina de lugares_compartido
        // Rechazar elimina de lugares_compartidos
        // SELECT FROM lugares_compartidos where usuarioReceptor
        //usuario_emisor 	latitud 	longitud 	altitud 	enlace 	usuario_receptor
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(Utils.INTENTS_EMAIL,emailReceptor);
        this.startActivity(intent);
        this.finish();

    }

    @Override
    protected void onResume() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.conmigo);
        title = getSupportActionBar().getTitle().toString();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Utils.menuOption(this, item, emailReceptor, title);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return Utils.createMenu(menu, this);
    }
}
