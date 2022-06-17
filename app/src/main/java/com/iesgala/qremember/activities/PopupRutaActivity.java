package com.iesgala.qremember.activities;

import android.content.Intent;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.PopupRutaController;
import com.iesgala.qremember.model.BasicLugar;
import com.iesgala.qremember.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Actividad lanzada al pulsar sobre el botón de ver ruta en la actividad RutasActivity
 * muestra las categorias de la ruta, un mápa con las coordenadas de los lugares de la ruta marcadas
 * y los controles para eliminar o compartir la ruta. Implementa la interfaz OnMapReadyCallback
 * para controlar los callback que realiza el mapa.
 * @author David Dorado
 * @version 1.0
 */
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
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

    /**
     * Método encargado de inicializar los textos y controles de la actividad excepto el mapa
     */
    private void initialize() {
        TextView tvNombreRuta = findViewById(R.id.tvNombreRutaPopup);
        tvNombreRuta.setText(getString(R.string.mapa_de_ruta) + nombreRuta);
        Button btnCompartirRuta = findViewById(R.id.btnCompartirRuta);
        Button btnEliminarRuta = findViewById(R.id.btnEliminarRuta);
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

    /**
     * Método encargado de añadir marcadores al mapa a partir de una lista de objetos LatLng y
     * finalmente pone el mapa enfocando el último lugar de la lista acercando el zoom a ese lugar
     * @param map Mapa en el cual poner los marcadores
     */
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        ArrayList<LatLng> locationArrayList = new ArrayList<>();
        for (int i = 0; i < lugares.size(); i++) {
            locationArrayList.add(new LatLng(lugares.get(i).getLatitud(),lugares.get(i).getLongitud()));
        }
        for (int i = 0; i < locationArrayList.size(); i++) {
            map.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title(lugares.get(i).getNombre()));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(locationArrayList.get(i),14.0f));

        }
    }

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
