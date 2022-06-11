package com.iesgala.qremember.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.iesgala.qremember.utils.Utils;

import java.util.ArrayList;

/**
 * @author David Dorado
 * @version 1.0
 */
public class PopupLugarActivity extends AppCompatActivity {
    static final int POPUPLUGAR_ACTIVITY_CODE = 78;
    String emailUsuario;
    String enlace;
    int posicion;
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
                ArrayList<Lugar> lugares = MainActivityController.obtenerLugares(this, emailUsuario);
                getSupportActionBar().setTitle(lugares.get(posicion).getNombre());
                TextView tvCategorias = findViewById(R.id.tvCategoriasPopup);
                String categorias = "";
                for (Categoria c : lugares.get(posicion).getCategorias()){
                    categorias = categorias+c.getNombre()+" ";
                }
                tvCategorias.setText(categorias);
            } else {
                Utils.AlertDialogGenerate(this, getString(R.string.err), "Error recuperando imagenes");
            }
        }catch (Exception e){
            e.printStackTrace();
            Utils.AlertDialogGenerate(this,getString(R.string.err),e.getMessage());
        }
    }

    private void setAdapter(Intent data) {
        enlace = data.getStringExtra(Utils.INTENTS_ENLACE);
        ArrayList<Imagen> imagenes = PopupLugarController.obtenerImagenes(this, enlace);
        if (imagenes != null && imagenes.size() > 0) {
            ImagesAdapter imagesAdapter = new ImagesAdapter(this, imagenes);
            ListView lvImagenes = findViewById(R.id.lvImagenes);
            lvImagenes.setAdapter(imagesAdapter);
            Button btnCompartir = findViewById(R.id.btnCompartir);
            btnCompartir.setOnClickListener(l -> PopupLugarController.compartir());
            Button btnModificar = findViewById(R.id.btnModificar);
            btnModificar.setOnClickListener(l -> PopupLugarController.modificar());
            Button btnEliminar = findViewById(R.id.btnEliminar);
            btnEliminar.setOnClickListener(l -> PopupLugarController.eliminar());
            Button btnNuevaImagen = findViewById(R.id.btnNuevaImagen);
            btnNuevaImagen.setOnClickListener(l -> PopupLugarController.nuevaImagen());
            Button btnEliminarImagen = findViewById(R.id.btnEliminarImagen);
            btnEliminarImagen.setOnClickListener(l -> PopupLugarController.eliminarImagen(imagenes));
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
        setResult(POPUPLUGAR_ACTIVITY_CODE, intent);
        super.onBackPressed();
    }
}
