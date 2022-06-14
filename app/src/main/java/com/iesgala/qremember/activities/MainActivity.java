package com.iesgala.qremember.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.iesgala.qremember.R;
import com.iesgala.qremember.adapters.LugaresAdapter;
import com.iesgala.qremember.controllers.MainActivityController;
import com.iesgala.qremember.model.Lugar;
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
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    LocationManager locManager;
    Intent intent;
    String emailUsuario;
    Button btnNuevoLugar;
    String title;
    ArrayList<String> nombresCategoria = new ArrayList<>();
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == PopupLugarActivity.POPUPLUGAR_ACTIVITY_CODE || result.getResultCode() == NuevoLugarActivity.NUEVOLUGARACTIVITY_CODE) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        setAdapter(intent);
                    }
                }
            }
    );

    @Override
    protected void onResume() {
        Intent intent = new Intent();
        intent.putExtra(Utils.INTENTS_EMAIL, emailUsuario);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.lugares);
        title = getSupportActionBar().getTitle().toString();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setAdapter(intent);
        filtroCategorias();
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = getIntent();
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.lugares);
        title = getSupportActionBar().getTitle().toString();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        filtroCategorias();
        setAdapter(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Utils.menuOption(this, item, emailUsuario, title);
        return true;
    }

    private void setAdapter(Intent data) {
        emailUsuario = data.getStringExtra(Utils.INTENTS_EMAIL);
        ArrayList<Lugar> lugares = MainActivityController.obtenerLugares(this, emailUsuario);
        btnNuevoLugar = findViewById(R.id.btnNuevoLugar);
        if (lugares != null) {
            LugaresAdapter lugaresAdapter = new LugaresAdapter(this, lugares);
            ListView lvLugares = findViewById(R.id.lvLugares);
            lvLugares.setClickable(true);
            lvLugares.setAdapter(lugaresAdapter);
            lvLugares.setOnItemClickListener((adapterView, view, i, l) -> MainActivityController.clickLugar(
                    this,
                    i,
                    lugares.get(i).getEnlace(),
                    emailUsuario,
                    lugares.get(i).getLongitud(),
                    lugares.get(i).getLatitud(),
                    lugares.get(i).getAltitud(),
                    lugares.get(i).getNombre()
            ));
        }
        btnNuevoLugar.setOnClickListener(l -> MainActivityController.nuevoLugar(this));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (data.getStringExtra(Utils.INTENTS_EMAIL) != null) {
                setAdapter(data);
            }
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                rastreoGPS(result.getContents());
            } else Toast.makeText(this, "Leer cancelado", Toast.LENGTH_LONG).show();
        } else {
            Utils.AlertDialogGenerate(this, getString(R.string.err), getString(R.string.err_qr_lectura));
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return Utils.createMenu(menu, this);
    }

    private void rastreoGPS(String qrResult) {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utils.AlertDialogGenerate(this, getString(R.string.err), getString(R.string.err_ubicacion));
            return;
        }
        Location locGps = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        MainActivityController.formularioNuevoLugar(locGps, qrResult, emailUsuario, MainActivity.this);

    }

    private void filtroCategorias() {
        try {
            String sql = "SELECT nombre FROM categoria";
            ResultSet resultSet = new AsyncTasks.SelectTask().execute(sql).get(1, TimeUnit.MINUTES);
            if (nombresCategoria == null || nombresCategoria.size() == 0) {
                nombresCategoria.add("Todos");
            }
            if (resultSet != null) {
                while (resultSet.next()) {
                    nombresCategoria.add(resultSet.getString("nombre"));
                }
                Spinner spFiltroCategorias = findViewById(R.id.spFiltroCategorias);
                spFiltroCategorias.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nombresCategoria));
                spFiltroCategorias.setOnItemSelectedListener(this);
            }
        } catch (ExecutionException | TimeoutException | InterruptedException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            onNothingSelected(parent);
        } else {
            ArrayList<Lugar> lugaresFiltrados = MainActivityController.obtenerLugaresFiltrados(this, emailUsuario, nombresCategoria.get(position));
            if (lugaresFiltrados != null) {
                LugaresAdapter lugaresAdapter = new LugaresAdapter(this, lugaresFiltrados);
                ListView lvLugares = findViewById(R.id.lvLugares);
                lvLugares.setClickable(true);
                lvLugares.setAdapter(lugaresAdapter);
                lvLugares.setOnItemClickListener((adapterView, v, i, l) -> MainActivityController.clickLugar(
                        this,
                        i,
                        lugaresFiltrados.get(i).getEnlace(),
                        emailUsuario,
                        lugaresFiltrados.get(i).getLongitud(),
                        lugaresFiltrados.get(i).getLatitud(),
                        lugaresFiltrados.get(i).getAltitud(),
                        lugaresFiltrados.get(i).getNombre()
                ));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        ArrayList<Lugar> lugares = MainActivityController.obtenerLugares(this, emailUsuario);
        if (lugares != null) {
            LugaresAdapter lugaresAdapter = new LugaresAdapter(this, lugares);
            ListView lvLugares = findViewById(R.id.lvLugares);
            lvLugares.setClickable(true);
            lvLugares.setAdapter(lugaresAdapter);
            lvLugares.setOnItemClickListener((adapterView, view, i, l) -> MainActivityController.clickLugar(
                    this,
                    i,
                    lugares.get(i).getEnlace(),
                    emailUsuario,
                    lugares.get(i).getLongitud(),
                    lugares.get(i).getLatitud(),
                    lugares.get(i).getAltitud(),
                    lugares.get(i).getNombre()
            ));
        }

    }
}
