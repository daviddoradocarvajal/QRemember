package com.iesgala.qremember.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.RecuperarContraseniaController;

import java.util.Objects;

/**
 * Actividad encargada de mostrar el formulario de recuperación de contraseña al usuario
 * el usuario rellena su email y las pregunta/respuesta de seguridad que introdujo al crear su
 * cuenta. Si coinciden se le permite recuperar su contraseña. Si falla se le permite volver a
 * intentarlo hasta 3 veces, después de 3 intentos aparece un mensaje y finaliza la actividad
 * @author David Dorado
 * @version 1.0
 */
public class RecuperarContraseniaActivity extends AppCompatActivity {
    int intentos=0;
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
        btnComprobar.setOnClickListener(l -> {
            if(intentos<3){
                if(!RecuperarContraseniaController.recuperarContrasenia(this)){
                    intentos++;
                }
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.err));
                builder.setMessage(getString(R.string.err_intentos));
                builder.setPositiveButton(R.string.confirmar, (dialogInterface, i) -> this.finish());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

}

