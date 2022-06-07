package com.iesgala.qremember.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
public class NuevaContraseniaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevacontrasenia);
        String email = getIntent().getStringExtra("Usuario");
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.recuperando_contrasenia)+email);
        TextView tvNuevaPass = findViewById(R.id.tvNuevaPass);
        TextView tvNuevaPassRe = findViewById(R.id.tvNuevaPassRe);
        TextView tvRecup = findViewById(R.id.tvRecup);
        TextView tvErrorNuevaPass = findViewById(R.id.tvErrorNuevaPass);
        tvRecup.setText(getString(R.string.recuperando_contrasenia)+email);
        Button btnConfirmarNuevaPass = findViewById(R.id.btnConfirmarNuevaPass);
        btnConfirmarNuevaPass.setOnClickListener(i -> {
            if (tvNuevaPass.getText().equals(tvNuevaPassRe.getText())){

            }else {
                tvErrorNuevaPass.setText(R.string.no_coinciden);
                tvErrorNuevaPass.setVisibility(View.VISIBLE);
            }
        });




    }
}
