package com.iesgala.qremember.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.RegisterActivityController;
import com.iesgala.qremember.utils.Utils;

import java.util.Objects;
/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Registrar nuevo usuario");
        Button btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);
        Spinner spPreguntas = findViewById(R.id.spPreguntas);
        ArrayAdapter<CharSequence> spPreguntasAdapter = ArrayAdapter.createFromResource(this,R.array.Preguntas, android.R.layout.simple_spinner_item);
        spPreguntasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPreguntas.setAdapter(spPreguntasAdapter);
        btnRegistrarUsuario.setOnClickListener(l ->{
            if(spPreguntas.getSelectedItem() != null && !spPreguntas.getSelectedItem().equals(getResources().getStringArray(R.array.Preguntas)[0]))
                RegisterActivityController.registrarUsuario(this);
            else
                Utils.AlertDialogGenerate(this,getString(R.string.err),getString(R.string.seleccione_pregunta));
                });
    }
}
