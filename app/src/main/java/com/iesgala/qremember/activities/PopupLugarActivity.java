package com.iesgala.qremember.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.adapters.ImagesAdapter;
import com.iesgala.qremember.controllers.MainActivityController;
import com.iesgala.qremember.controllers.PopupLugarController;
import com.iesgala.qremember.model.Categoria;
import com.iesgala.qremember.model.Imagen;
import com.iesgala.qremember.model.Lugar;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Actividad lanzada al pulsar sobre un lugar en la actividad MainActivity para mostrar
 * las imagenes del lugar y los controles para modificar, compartir y eliminar el lugar por una
 * parte y por otra los controles de añadir una imagen al lugar o eliminar una o varias imágenes
 * @author David Dorado
 * @version 1.0
 */
public class PopupLugarActivity extends AppCompatActivity {
    static final int POPUPLUGAR_ACTIVITY_CODE = 78;
    String emailUsuario;
    String enlace;
    String longitud;
    String latitud;
    String altitud;
    String nombreLugar;
    int posicion;
    ListView lvImagenes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_lugar);
        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);
        int ancho = medidasVentana.widthPixels;
        int alto = medidasVentana.heightPixels;
        try {
            getWindow().setLayout((int) (ancho * 0.95), (int) (alto * 0.95));
            Intent intent = getIntent();
            if (intent != null) {
                setAdapter(intent);
                emailUsuario = intent.getStringExtra(Utils.INTENTS_EMAIL);
                posicion = intent.getIntExtra(Utils.INTENTS_POSICION, 1);
                TextView tvCategorias = findViewById(R.id.tvCategoriasPopup);
                ArrayList<Lugar> lugares = MainActivityController.obtenerLugares(this, emailUsuario);
                if(lugares!=null) {
                    Objects.requireNonNull(getSupportActionBar()).setTitle(lugares.get(posicion).getNombre());
                    String categorias = "";
                    for (Categoria c : lugares.get(posicion).getCategorias()) {
                        categorias = categorias + c.getNombre() + " ";
                    }
                    tvCategorias.setText(categorias);
                }
            } else {
                Utils.AlertDialogGenerate(this, getString(R.string.err), "Error recuperando imagenes");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.AlertDialogGenerate(this, getString(R.string.err), e.getMessage());
        }
    }

    private void setAdapter(Intent data) {
        enlace = data.getStringExtra(Utils.INTENTS_ENLACE);
        longitud = data.getStringExtra(Utils.INTENTS_LONGITUD);
        latitud = data.getStringExtra(Utils.INTENTS_LATITUD);
        altitud = data.getStringExtra(Utils.INTENTS_ALTITUD);
        nombreLugar = data.getStringExtra(Utils.INTENTS_NOMBRE_LUGAR);
        ArrayList<Imagen> imagenes = PopupLugarController.obtenerImagenes(this, enlace);
        if (imagenes.size() > 0) {
            ImagesAdapter imagesAdapter = new ImagesAdapter(this, imagenes);
            lvImagenes = findViewById(R.id.lvImagenes);
            lvImagenes.setAdapter(imagesAdapter);
            lvImagenes.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        }
        Button btnCompartir = findViewById(R.id.btnCompartir);
        btnCompartir.setOnClickListener(l -> PopupLugarController.compartir(this,emailUsuario,enlace,longitud,latitud,altitud));
        Button btnModificar = findViewById(R.id.btnModificar);
        btnModificar.setOnClickListener(l -> PopupLugarController.modificar(this,enlace,longitud,latitud,altitud,nombreLugar,emailUsuario));
        Button btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(l -> PopupLugarController.eliminarLugar(this,enlace,emailUsuario));
        Button btnNuevaImagen = findViewById(R.id.btnNuevaImagen);
        btnNuevaImagen.setOnClickListener(l -> PopupLugarController.nuevaImagen(this));
        Button btnEliminarImagen = findViewById(R.id.btnEliminarImagen);
        btnEliminarImagen.setOnClickListener(l -> {
            if(lvImagenes.getCheckedItemCount()>=1)
            PopupLugarController.eliminarImagenes(this, imagenes,emailUsuario);
            else Utils.AlertDialogGenerate(this,getString(R.string.msg_aviso),getString(R.string.msg_selecciona_imagen));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == NuevoLugarActivity.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap bm = (Bitmap) extras.get("data");
                    ByteArrayOutputStream blob = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, blob);
                    byte[] bmData = blob.toByteArray();
                    new AsyncTasks.PreparedInsertImageTask().execute(bmData,longitud,latitud,altitud,enlace).get(1, TimeUnit.MINUTES);
                    bm.recycle();
                    try {
                        blob.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    data.putExtra(Utils.INTENTS_ENLACE,enlace);
                    setAdapter(data);
                }
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        setResult(POPUPLUGAR_ACTIVITY_CODE, intent);
        super.onBackPressed();
    }
}
