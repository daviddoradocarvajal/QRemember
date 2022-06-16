package com.iesgala.qremember.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.NuevaContraseniaController;
import com.iesgala.qremember.utils.Utils;

import java.util.Objects;

/**
 * Actividad encargada de cambiar la contraseña del usuario que ya se ha comprobado su identidad
 * a través de su pregunta/respuesta de seguridad
 * @author David Dorado
 * @version 1.0
 */
public class NuevaContraseniaActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevacontrasenia);
        String email = getIntent().getStringExtra(Utils.INTENTS_EMAIL);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.recuperando_contrasenia) + email);
        TextView tvInfoNuevaPass = findViewById(R.id.tvInfoNuevaPass);
        tvInfoNuevaPass.setText(getString(R.string.recuperando_contrasenia) + " " + email);
        Button btnConfirmarNuevaPass = findViewById(R.id.btnConfirmarNuevaPass);
        btnConfirmarNuevaPass.setOnClickListener(i -> NuevaContraseniaController.actualizarContrasenia(this, email));
    }

}
