package com.iesgala.qremember.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;

import com.iesgala.qremember.adapters.RutasAdapter;

import com.iesgala.qremember.controllers.RutasController;

import com.iesgala.qremember.model.Ruta;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Clase con la actividad encargada de las rutas, se accede desde el menú y muestra las rutas
 * del usuario junto con un botón para crear nuevas rutas. Implementa la interfaz
 * OnItemSelectedListener que permite manejar el evento del spinner para filtrar por categoria
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class RutasActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Intent intent;
    String emailUsuario;
    Button btnNuevaRuta;
    String title;
    final ArrayList<String> nombresCategoria = new ArrayList<>();


    @Override
    protected void onResume() {
        Intent intent = new Intent();
        intent.putExtra(Utils.INTENTS_EMAIL, emailUsuario);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.rutas);
        title = Objects.requireNonNull(getSupportActionBar().getTitle()).toString();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setAdapter(intent);
        filtroCategorias();
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);
        intent = getIntent();
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.rutas);
        title = Objects.requireNonNull(getSupportActionBar().getTitle()).toString();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        filtroCategorias();
        setAdapter(intent);
    }

    private void setAdapter(Intent data){
        emailUsuario = data.getStringExtra(Utils.INTENTS_EMAIL);
        ArrayList<Ruta> rutas = RutasController.obtenerRutas(this, emailUsuario);
        btnNuevaRuta = findViewById(R.id.btnNuevaRuta);
        if (rutas != null) {
            RutasAdapter rutasAdapter = new RutasAdapter(this, rutas);
            ListView lvRutas = findViewById(R.id.lvRutas);
            lvRutas.setAdapter(rutasAdapter);
        }
        btnNuevaRuta.setOnClickListener(l -> RutasController.nuevaRuta(this,emailUsuario));
    }

    private void filtroCategorias(){
        try {
            String sql = "SELECT nombre FROM categoria";
            ResultSet resultSet = new AsyncTasks.SelectTask().execute(sql).get(1, TimeUnit.MINUTES);
            if (nombresCategoria == null || nombresCategoria.size() == 0) {
                Objects.requireNonNull(nombresCategoria).add("Todos");
            }
            if (resultSet != null) {
                while (resultSet.next()) {
                    nombresCategoria.add(resultSet.getString("nombre"));
                }
                Spinner spCategoriasRutas = findViewById(R.id.spCategoriasRutas);
                spCategoriasRutas.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nombresCategoria));
                spCategoriasRutas.setOnItemSelectedListener(this);
            }
        } catch (ExecutionException | TimeoutException | InterruptedException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return Utils.createMenu(menu, this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Utils.menuOption(this, item, emailUsuario, title);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            onNothingSelected(parent);
        } else {
            ArrayList<Ruta> rutasFiltradas = RutasController.obtenerRutasFiltradas(this, emailUsuario, nombresCategoria.get(position));
            if (rutasFiltradas != null) {
                RutasAdapter rutasAdapter = new RutasAdapter(this, rutasFiltradas);
                ListView lvRutas = findViewById(R.id.lvRutas);
                lvRutas.setAdapter(rutasAdapter);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        ArrayList<Ruta> rutas = RutasController.obtenerRutas(this, emailUsuario);
        if (rutas != null) {
            RutasAdapter rutasAdapter = new RutasAdapter(this, rutas);
            ListView lvRutas = findViewById(R.id.lvRutas);
            lvRutas.setAdapter(rutasAdapter);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Utils.INTENTS_EMAIL,emailUsuario);
        setResult(PopupLugarActivity.POPUPLUGAR_ACTIVITY_CODE, intent);
        super.onBackPressed();
    }
}
