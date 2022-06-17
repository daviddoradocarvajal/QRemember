package com.iesgala.qremember.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.EliminarCuentaController;
import com.iesgala.qremember.utils.Utils;

import java.util.Objects;

/**
 * Clase actividad con los campos de texto para comprobar que es el usuario el que está intentando
 * eliminar su cuenta y un boton para confirmar la eliminación de la cuenta
 * @author David Dorado
 * @version 1.0
 */
public class EliminarCuentaActivity extends AppCompatActivity {
    String emailUsuario;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminarcuenta);
        Intent intent = getIntent();
        if (intent!=null){
            emailUsuario = intent.getStringExtra(Utils.INTENTS_EMAIL);
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.eliminar_usuario));
        TextView tvEliminarEmail = findViewById(R.id.tvEliminarEmail);
        TextView tvEliminarPass = findViewById(R.id.tvEliminarPass);
        Button btnEliminarconfirmar = findViewById(R.id.btnEliminarConfirmar);
        btnEliminarconfirmar.setOnClickListener(l -> {
            if(!tvEliminarEmail.getText().toString().isEmpty() && !tvEliminarPass.getText().toString().isEmpty())
            EliminarCuentaController.eliminarCuenta(this,tvEliminarEmail.getText().toString(),tvEliminarPass.getText().toString());
            else Utils.AlertDialogGenerate(this,getString(R.string.msg_aviso),getString(R.string.msg_campo_vacio));
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(Utils.INTENTS_EMAIL,emailUsuario);
        this.startActivity(intent);
        this.finish();
    }
}
