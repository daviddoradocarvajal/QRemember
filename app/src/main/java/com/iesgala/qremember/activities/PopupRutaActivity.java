package com.iesgala.qremember.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.PopupRutaController;
import com.iesgala.qremember.model.BasicLugar;
import com.iesgala.qremember.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class PopupRutaActivity extends AppCompatActivity implements OnMapReadyCallback {
    static final int POPUPRUTA_ACTIVITY_CODE = 77;
    String emailUsuario;
    String nombreRuta;
    ArrayList<BasicLugar> lugares;
    private MapView mapViewPopup;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        setContentView(R.layout.activity_popup_ruta);
        try {
            Intent intent = getIntent();
            emailUsuario = intent.getStringExtra(Utils.INTENTS_EMAIL);
            nombreRuta = intent.getStringExtra(Utils.INTENTS_NOMBRE_RUTA);
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.ruta) + " " + nombreRuta);
            lugares = (ArrayList<BasicLugar>) intent.getSerializableExtra(Utils.INTENTS_BASICLUGAR);
            initialize();
            mapViewPopup = findViewById(R.id.mapViewPopup);
            mapViewPopup.onCreate(mapViewBundle);
            mapViewPopup.getMapAsync(this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapViewPopup.onSaveInstanceState(mapViewBundle);
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
        setResult(POPUPRUTA_ACTIVITY_CODE, intent);
        super.onBackPressed();
    }

    private void initialize() {
        TextView tvNombreRuta = findViewById(R.id.tvNombreRutaPopup);
        tvNombreRuta.setText(getString(R.string.mapa_de_ruta) + nombreRuta);
        Button btnModificarRuta = findViewById(R.id.btnModificarRuta);
        Button btnCompartirRuta = findViewById(R.id.btnCompartirRuta);
        Button btnEliminarRuta = findViewById(R.id.btnEliminarRuta);
        btnModificarRuta.setOnClickListener(l -> PopupRutaController.modificarRuta(this, emailUsuario, nombreRuta));
        btnCompartirRuta.setOnClickListener(l -> PopupRutaController.compartirRuta(this, emailUsuario, nombreRuta));
        btnEliminarRuta.setOnClickListener(l -> PopupRutaController.eliminarRuta(this, emailUsuario, nombreRuta));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapViewPopup.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapViewPopup.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapViewPopup.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        ArrayList<LatLng> locationArrayList = new ArrayList<>();
        LatLng sydney = new LatLng(-34, 151);
        LatLng TamWorth = new LatLng(-31.083332, 150.916672);
        LatLng NewCastle = new LatLng(-32.916668, 151.750000);
        LatLng Brisbane = new LatLng(-27.470125, 153.021072);
        locationArrayList.add(sydney);
        locationArrayList.add(TamWorth);
        for (int i = 0; i < lugares.size(); i++) {

            // below line is use to add marker to each location of our array list.
            map.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"));

            // below lin is use to zoom our camera on map.
            map.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

            // below line is use to move our camera to the specific location.
            map.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
        }
    }

//map.addMarker(new MarkerOptions().position(new LatLng(lugares.get(0).getLatitud(), lugares.get(0).getLongitud())).title("Marker"));
//map.addMarker(new MarkerOptions().position(new LatLng(lugares.get(1).getLatitud(), lugares.get(1).getLongitud())).title("Marker"));


    @Override
    protected void onPause() {
        mapViewPopup.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapViewPopup.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapViewPopup.onLowMemory();
    }
}
