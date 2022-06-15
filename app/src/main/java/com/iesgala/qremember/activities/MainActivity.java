package com.iesgala.qremember.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.util.List;
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
    final ArrayList<String> nombresCategoria = new ArrayList<>();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        emailUsuario = intent.getStringExtra(Utils.INTENTS_EMAIL);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.lugares);
        title = Objects.requireNonNull(getSupportActionBar().getTitle()).toString();
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
        title = Objects.requireNonNull(getSupportActionBar().getTitle()).toString();
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

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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

    public boolean checkAndRequestPermissions() {
        int internet = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int loc = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), 1);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("MissingPermission")
    private void rastreoGPS(String qrResult) {
        checkAndRequestPermissions();
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        }
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},PackageManager.PERMISSION_GRANTED);
        }
        final Location[] actual = new Location[1];
         @SuppressLint("MissingPermission") Location locGps = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
         @SuppressLint("MissingPermission") Location locGsm = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        locManager.getCurrentLocation(
                LocationManager.GPS_PROVIDER,
                null,
                this.getMainExecutor(),
                location -> actual[0] = location);
        if(actual[0]!=null){
            MainActivityController.formularioNuevoLugar(actual[0], qrResult, emailUsuario, MainActivity.this);
        }
        if(locGps!=null){
            if(locGsm!=null){
                if(locGps.getAccuracy()<locGsm.getAccuracy()){
                    MainActivityController.formularioNuevoLugar(locGps, qrResult, emailUsuario, MainActivity.this);
                }else
                MainActivityController.formularioNuevoLugar(locGsm, qrResult, emailUsuario, MainActivity.this);
            }
            MainActivityController.formularioNuevoLugar(locGps, qrResult, emailUsuario, MainActivity.this);
        }
        MainActivityController.formularioNuevoLugar(locGsm, qrResult, emailUsuario, MainActivity.this);




    }

    private void filtroCategorias() {
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
