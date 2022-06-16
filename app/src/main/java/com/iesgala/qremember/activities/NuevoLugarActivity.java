package com.iesgala.qremember.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.NuevoLugarController;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Actividad encargada de almacenar un nuevo lugar en la cuenta del usuario a través de los datos
 * que introduce el usuario, que son nombre y categorias del lugar (mínimo 1)
 * @author David Dorado
 * @version 1.0
 */
public class NuevoLugarActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private String longitud;
    private String latitud;
    private String altitud;
    private String enlace;
    private EditText tvNombreLugar;
    private ArrayList<String> categoriasSeleccionadas;
    private String emailUsuario;
    public static final int NUEVOLUGARACTIVITY_CODE = 79;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevolugar);
        Intent intent = getIntent();
        longitud = intent.getStringExtra(Utils.INTENTS_LONGITUD);
        latitud = intent.getStringExtra(Utils.INTENTS_LATITUD);
        altitud = intent.getStringExtra(Utils.INTENTS_ALTITUD);
        enlace = intent.getStringExtra(Utils.INTENTS_ENLACE);
        emailUsuario = intent.getStringExtra(Utils.INTENTS_EMAIL);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.nuevo_lugar);
        setCategorias();
    }

    /**
     * Método para tomar una foto y devolver el resultado en evento de ActivityResult
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ListView lvMultiple = findViewById(R.id.lvCategoriasNuevoLugar);
        categoriasSeleccionadas = new ArrayList<>();
        SparseBooleanArray checked = lvMultiple.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++)
            if (checked.valueAt(i)) {
                categoriasSeleccionadas.add(adapterView.getItemAtPosition(checked.keyAt(i)).toString());
            }
    }

    @Override
    protected void onResume() {
        setCategorias();
        super.onResume();
    }
    private void setCategorias(){
        try {
            ArrayList<String> nombresCategoria = new ArrayList<>();
            String sql = "SELECT nombre FROM categoria";
            ResultSet resultSet = new AsyncTasks.SelectTask().execute(sql).get(1, TimeUnit.MINUTES);
            if(resultSet != null) {
                while (resultSet.next()) {
                    nombresCategoria.add(resultSet.getString("nombre"));
                }
                tvNombreLugar = findViewById(R.id.tvNombreLugar);
                ListView lv = findViewById(R.id.lvCategoriasNuevoLugar);
                lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, nombresCategoria));
                lv.setOnItemClickListener(this);
                Button btnTomarFoto = findViewById(R.id.btnTomarFoto);
                btnTomarFoto.setOnClickListener(l -> {
                    if(!tvNombreLugar.getText().toString().isEmpty()){
                        if(lv.getCheckedItemCount()>=1){
                            dispatchTakePictureIntent();
                        }else Utils.AlertDialogGenerate(this,getString(R.string.msg_aviso),getString(R.string.aviso_selecciona_categoria));
                    }else Utils.AlertDialogGenerate(this,getString(R.string.msg_aviso),getString(R.string.aviso_nombre));

                });
            }
        } catch (ExecutionException | InterruptedException | TimeoutException | SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setCategorias();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bm = (Bitmap) extras.get("data");
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, blob);
            byte[] bmData = blob.toByteArray();
            int respuesta = NuevoLugarController.nuevoLugar(this, longitud, latitud, altitud, enlace, tvNombreLugar.getText().toString(), emailUsuario, bmData, categoriasSeleccionadas);
            bm.recycle();
            try {
                blob.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            NuevoLugarController.finalizar(this,emailUsuario,respuesta);
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
        intent.putExtra(Utils.INTENTS_EMAIL,emailUsuario);
        super.onBackPressed();
    }
}
