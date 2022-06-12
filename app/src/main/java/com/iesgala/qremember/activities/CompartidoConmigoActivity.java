package com.iesgala.qremember.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.utils.Utils;

import java.util.Objects;

/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class CompartidoConmigoActivity extends AppCompatActivity {
    String emailUsuario;
    String title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compartidoconmigo);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.conmigo);
        title = getSupportActionBar().getTitle().toString();

        // emailUsuario = usuario_receptor
        //usuario_emisor 	latitud 	longitud 	altitud 	enlace 	usuario_receptor
    }

    @Override
    protected void onResume() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.conmigo);
        title = getSupportActionBar().getTitle().toString();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Utils.menuOption(this,item,emailUsuario,title);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return Utils.createMenu(menu,this);
    }
}
