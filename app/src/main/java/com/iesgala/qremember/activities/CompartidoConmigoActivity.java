package com.iesgala.qremember.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.utils.Utils;

/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class CompartidoConmigoActivity extends AppCompatActivity {
    String emailUsuario;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // emailUsuario = usuario_receptor
        //usuario_emisor 	latitud 	longitud 	altitud 	enlace 	usuario_receptor
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Utils.menuOption(this,item,emailUsuario);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return Utils.createMenu(menu,this);
    }
}
