package com.iesgala.qremember.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
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
import com.iesgala.qremember.model.Categoria;
import com.iesgala.qremember.model.Imagen;
import com.iesgala.qremember.model.Lugar;

import com.iesgala.qremember.utils.Utils;
import com.iesgala.qremember.utils.FakeDb;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    LocationManager locManager;
    Intent intent;
    String emailUsuario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = getIntent();
        emailUsuario = intent.getStringExtra("Email");
        String nombreUsuario = intent.getStringExtra("Nombre");
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lugares de " + nombreUsuario);

        try {
            new SelectUsuarioTask().execute(emailUsuario).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                rastreoGPS(result.getContents());
            } else Toast.makeText(this, "Leer cancelado", Toast.LENGTH_LONG).show();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private class SelectUsuarioTask extends AsyncTask<String, Void, ArrayList<Lugar>> {
        Connection conn;
        ArrayList<Lugar> lugares = new ArrayList<>();
        @Override
        protected ArrayList<Lugar> doInBackground(String... strings) {
            if (conn == null) {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                    Statement statement = conn.createStatement();
                    Statement statement1 = conn.createStatement();
                    Statement statement2 = conn.createStatement();
                    System.out.println(strings[0]);
                    ResultSet resultSetLugares = statement.executeQuery("SELECT longitud,latitud,altitud,enlace,nombre FROM Lugar WHERE email_usuario='" + strings[0] + "'");
                    while (resultSetLugares.next()) {
                        ResultSet resultSetImagenes = statement1.executeQuery("SELECT id,imagen,longitud,latitud,altitud FROM Imagen WHERE longitud = " + resultSetLugares.getFloat("longitud") + " AND latitud = " + resultSetLugares.getFloat("latitud") + " AND altitud = " + resultSetLugares.getFloat("altitud") + ";");
                        ArrayList<Imagen> imagenes = new ArrayList<>();
                        while (resultSetImagenes.next()) {
                            InputStream stream = resultSetImagenes.getBlob("imagen").getBinaryStream();
                            imagenes.add(new Imagen(resultSetImagenes.getInt("id"), Drawable.createFromStream(stream,"imagen")));
                        }
                        ResultSet resultSetCategorias = statement2.executeQuery("SELECT nombre_categoria FROM Lugar_Categoria WHERE longitud = "+resultSetLugares.getFloat("longitud")+" AND latitud = "+resultSetLugares.getFloat("latitud")+" AND altitud = "+resultSetLugares.getFloat("altitud")+";");
                        ArrayList<Categoria> categorias = new ArrayList<>();
                        while (resultSetCategorias.next()) {
                            categorias.add(new Categoria(resultSetCategorias.getString("nombre_categoria")));
                        }
                        lugares.add(new Lugar(resultSetLugares.getFloat("longitud"),resultSetLugares.getFloat("latitud"),resultSetLugares.getFloat("altitud"),resultSetLugares.getString("enlace"),resultSetLugares.getString("nombre"),imagenes,categorias));
                    }

                    return lugares;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return lugares;
                }
            }
            return lugares;
        }

        @Override
        protected void onPostExecute(ArrayList<Lugar> lugares) {
            Button btnNuevoLugar = findViewById(R.id.btnNuevoLugar);
            LocalesAdapter localesAdapter = new LocalesAdapter(Objects.requireNonNull(Utils.getActivity(btnNuevoLugar)),  lugares);
            ListView lvLugares = findViewById(R.id.lvLugares);
            lvLugares.setClickable(true);
            lvLugares.setAdapter(localesAdapter);
            lvLugares.setOnItemClickListener((adapterView, view, i, l) -> {
                System.out.println(adapterView.getItemAtPosition(i));
                MainActivityController.clickLugar(Objects.requireNonNull(Utils.getActivity(btnNuevoLugar)), lugares.get(i));

            });

            btnNuevoLugar.setOnClickListener(l -> MainActivityController.nuevoLugar(Utils.getActivity(btnNuevoLugar)));

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return Utils.createMenu(menu, this);
    }

    private void rastreoGPS(String qrResult) {

        /*Se asigna a la clase LocationManager el servicio a nivel de sistema a partir del nombre.*/
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /*Se declara y asigna a la clase Location la última posición conocida proporcionada por el proveedor.*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
            return;
        }
        Location locGps = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locNetwork = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (locGps.getAccuracy()<locNetwork.getAccuracy())     MainActivityController.formularioNuevoLugar(locGps,qrResult,emailUsuario,MainActivity.this);
        else MainActivityController.formularioNuevoLugar(locNetwork,qrResult,emailUsuario,MainActivity.this);
    }


}
