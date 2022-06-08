package com.iesgala.qremember.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.iesgala.qremember.R;
import com.iesgala.qremember.adapters.LocalesAdapter;
import com.iesgala.qremember.controllers.MainActivityController;
import com.iesgala.qremember.model.Imagen;
import com.iesgala.qremember.model.Lugar;

import com.iesgala.qremember.model.Usuario;
import com.iesgala.qremember.utils.Config;
import com.iesgala.qremember.utils.FakeDb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    LocationManager locManager;
    LocationListener locListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String nombreUsuario = intent.getStringExtra("Nombre");
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lugares de " + nombreUsuario);
        String emailUsuario = intent.getStringExtra("Email");
        FakeDb db = new FakeDb(this);
        ArrayList<Lugar> lugs = new ArrayList<>();
        LocalesAdapter localesAdapter = new LocalesAdapter(this, db.lugares);
        ListView lvLugares = findViewById(R.id.lvLugares);
        lvLugares.setClickable(true);
        lvLugares.setAdapter(localesAdapter);
        lvLugares.setOnItemClickListener((adapterView, view, i, l) -> {
            System.out.println(adapterView.getItemAtPosition(i));
            MainActivityController.clickLugar(MainActivity.this, db.lugares.get(i));

        });
        Button btnNuevoLugar = findViewById(R.id.btnNuevoLugar);
        btnNuevoLugar.setOnClickListener(l -> MainActivityController.nuevoLugar(this));

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                System.out.println(result.getContents());
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                rastreoGPS();
            } else Toast.makeText(this, "Leer cancelado", Toast.LENGTH_LONG).show();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private class SelectUsuarioTask extends AsyncTask<String, Void, ArrayList<Lugar>> {
        Connection conn;

        @Override
        protected ArrayList<Lugar> doInBackground(String... strings) {
            if (conn == null) {
                try {
                    ArrayList<Lugar> lugares = new ArrayList<>();
                    conn = DriverManager.getConnection("jdbc:mysql://" + Config.SERVIDOR + ":" + Config.PUERTO + "/" + Config.BD + "", Config.USUARIO, Config.PASSWORD);
                    Statement statement = conn.createStatement();
                    ResultSet resultSetImagenes = statement.executeQuery("SELECT L.longitud as longitud,L.latitud as latitud,L.altitud as altitud,L.enlace as enlace,L.nombre as nombre,L.email_usuario as email_usuario,I.imagen as imagen,I.id as id FROM Lugar as L INNER JOIN Imagen as I ON L.longitud=I.longitud AND L.latitud=I.latitud AND L.altitud=I.altitud WHERE email_usuario='" + strings[0] + "'");
                   /*
                    Imagen imagen = new Imagen();
                    while (resultSetImagenes.next()) {
                        lugares.add(new Lugar(resultSetImagenes.getFloat("longitud"),resultSetImagenes.getFloat("latitud"),resultSetImagenes.getFloat("altitud"),
                                resultSetImagenes.getString("enlace"),resultSetImagenes.getString("nombre"),""))
                    }

                    */
                    return lugares;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return Config.createMenu(menu, this);
    }

    private void rastreoGPS() {

        /*Se asigna a la clase LocationManager el servicio a nivel de sistema a partir del nombre.*/
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /*Se declara y asigna a la clase Location la última posición conocida proporcionada por el proveedor.*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            return;
        }
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mostrarPosicion(loc);


        //Se define la interfaz LocationListener, que deberá implementarse con los siguientes métodos.
        locListener = new LocationListener() {
            //Método que será llamado cuando cambie la localización.
            @Override
            public void onLocationChanged(Location location) {
                mostrarPosicion(location);
            }

            //Método que será llamado cuando se produzcan cambios en el estado del proveedor.
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            //Método que será llamado cuando el proveedor esté habilitado para el usuario.
            @Override
            public void onProviderEnabled(String provider) {
            }

            //Método que será llamado cuando el proveedor esté deshabilitado para el usuario.
            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locListener);

    }

    private void mostrarPosicion(Location loc) {
        Toast.makeText(this, loc.getAltitude() + "\n" + loc.getLatitude() + "\n" + loc.getLongitude(), Toast.LENGTH_LONG).show();
    }


}
