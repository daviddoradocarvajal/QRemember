package com.iesgala.qremember.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.ModificarRutaController;
import com.iesgala.qremember.controllers.NuevaRutaController;
import com.iesgala.qremember.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class ModificarRutaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    String emailUsuario;
    String nombreRuta;
    ArrayList<String> lugaresSeleccionados;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_ruta);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.modificar_ruta+nombreRuta);
        Intent intent = getIntent();
        emailUsuario = intent.getStringExtra(Utils.INTENTS_EMAIL);
        nombreRuta = intent.getStringExtra(Utils.INTENTS_NOMBRE_RUTA);
        TextView tvModificarNombreRuta = findViewById(R.id.tvModificarNombreRuta);

        ListView lvLugaresModificarRuta = findViewById(R.id.lvLugaresModificarRuta);
        ListView lvCategorasModificarRuta = findViewById(R.id.lvCategoriasModificarRuta);
        lugaresSeleccionados = new ArrayList<>();
        Button btnModificarRutaConfirmar = findViewById(R.id.btnModificarRutaConfirmar);
        //btnModificarRutaConfirmar.setOnClickListener(l-> ModificarRutaController.modificarRuta());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView lvMultiple = findViewById(R.id.lvLugaresModificarRuta);
        lugaresSeleccionados = new ArrayList<>();
        SparseBooleanArray checked = lvMultiple.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            if (checked.valueAt(i)) {
                lugaresSeleccionados.add(parent.getItemAtPosition(checked.keyAt(i)).toString());
            }
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
        intent.putExtra(Utils.INTENTS_EMAIL, emailUsuario);
        super.onBackPressed();
    }
}
