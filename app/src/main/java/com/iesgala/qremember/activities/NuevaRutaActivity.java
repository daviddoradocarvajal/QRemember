package com.iesgala.qremember.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.NuevaRutaController;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NuevaRutaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    String emailUsuario;
    ArrayList<String> lugaresSeleccionados;
    ArrayList<String> categoriasSeleccionadasNuevaRuta;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_ruta);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.nueva_ruta);
        Intent intent = getIntent();
        emailUsuario = intent.getStringExtra(Utils.INTENTS_EMAIL);
        TextView tvNombreNuevaRuta = findViewById(R.id.tvNombreNuevaRuta);
        ListView lvNuevaRuta = findViewById(R.id.lvLugaresNuevaRuta);
        ListView lvCategorasNuevaRuta = findViewById(R.id.lvCategoriasNuevaRuta);
        setCategorias(lvCategorasNuevaRuta);
        lugaresSeleccionados = new ArrayList<>();
        Button btnNuevaRutaConfirmar = findViewById(R.id.btnNuevaRutaConfirmar);
        btnNuevaRutaConfirmar.setOnClickListener(l-> NuevaRutaController.nuevaRuta(this,emailUsuario,tvNombreNuevaRuta.getText().toString(),lugaresSeleccionados,categoriasSeleccionadasNuevaRuta));
    }

    private void setCategorias(ListView listView){
        try {
            ResultSet resultSet = new AsyncTasks.SelectTask().execute("SELECT nombre FROM categoria").get(1, TimeUnit.MINUTES);
            if (resultSet != null) {
                categoriasSeleccionadasNuevaRuta = new ArrayList<>();
                while (resultSet.next()) {
                    categoriasSeleccionadasNuevaRuta.add(resultSet.getString("nombre"));
                }
                listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, categoriasSeleccionadasNuevaRuta));
                listView.setOnItemClickListener(((parent, view, position, id) -> {
                    ListView lvMultiple = findViewById(R.id.lvCategoriasNuevaRuta);
                    categoriasSeleccionadasNuevaRuta = new ArrayList<>();
                    SparseBooleanArray checked = lvMultiple.getCheckedItemPositions();
                    for (int i = 0; i < checked.size(); i++) {
                        if (checked.valueAt(i)) {
                            categoriasSeleccionadasNuevaRuta.add(parent.getItemAtPosition(checked.keyAt(i)).toString());
                        }
                    }
                }));
            }
        }catch (SQLException | ExecutionException | InterruptedException | TimeoutException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView lvMultiple = findViewById(R.id.lvLugaresNuevaRuta);
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
