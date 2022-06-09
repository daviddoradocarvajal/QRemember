package com.iesgala.qremember.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;

import java.util.Objects;

/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class NuevoLugarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevolugar);
        Intent intent = getIntent();
        Float longitud = Float.parseFloat(intent.getStringExtra("longitud"));
        String latitud = intent.getStringExtra("latitud");
        String altitud = intent.getStringExtra("altitud");
        String enlace = intent.getStringExtra("enlace");
        String email = intent.getStringExtra("email");
        Objects.requireNonNull(getSupportActionBar()).setTitle("Nuevo lug "+email);

        TextView tvLocation = findViewById(R.id.tvLocation);
        TextView tvEnlace = findViewById(R.id.tvEnlace);
        tvLocation.setText(longitud + " "+ latitud + " "+altitud);
        tvEnlace.setText(enlace);
    }
}
