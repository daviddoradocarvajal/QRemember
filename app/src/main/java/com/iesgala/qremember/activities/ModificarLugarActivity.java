package com.iesgala.qremember.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.ModificarLugarController;
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
 *
 * @author David Dorado
 * @version 1.0
 */
public class ModificarLugarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String nombreLugar,emailUsuario;
    String enlaceLugar,longitud,latitud,altitud;
    ArrayList<String> categorias;
    ArrayList<String> categoriasSeleccionadas;
    Button btnModificar;
    ListView lvCategoriasModificar;
    TextView tvNombreLugarModificar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificarlugar);
        Intent intent = getIntent();
        if (intent != null) {
            nombreLugar = intent.getStringExtra(Utils.INTENTS_NOMBRE_LUGAR);
            enlaceLugar = intent.getStringExtra(Utils.INTENTS_ENLACE);
            longitud = intent.getStringExtra(Utils.INTENTS_LONGITUD);
            latitud = intent.getStringExtra(Utils.INTENTS_LATITUD);
            altitud = intent.getStringExtra(Utils.INTENTS_ALTITUD);
            emailUsuario = intent.getStringExtra(Utils.INTENTS_EMAIL);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Modificar " + nombreLugar);
        }
        lvCategoriasModificar = findViewById(R.id.lvCategoriasModificar);
        tvNombreLugarModificar = findViewById(R.id.tvNombreLugarModificar);
        btnModificar = findViewById(R.id.btnModificarLugar);
        btnModificar.setOnClickListener(l-> ModificarLugarController.modificarLugar(this,categoriasSeleccionadas,tvNombreLugarModificar.getText().toString(),enlaceLugar,longitud,latitud,altitud,emailUsuario));
        inicializarListView();


    }
    // Esto va en el controller?
    public void inicializarListView(){
        try {
            ResultSet resultSet = new AsyncTasks.SelectTask().execute("SELECT nombre FROM categoria").get(1, TimeUnit.MINUTES);
            if (resultSet != null) {
                categorias = new ArrayList<>();
                while (resultSet.next()) {
                    categorias.add(resultSet.getString("nombre"));
                }
                lvCategoriasModificar.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                lvCategoriasModificar.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, categorias));
                lvCategoriasModificar.setOnItemClickListener(this);
                ResultSet resultSetSeleccionadas = new AsyncTasks.SelectTask().execute("SELECT nombre_categoria FROM lugar_categoria WHERE enlace='"+enlaceLugar+"'").get(1,TimeUnit.MINUTES);
                if (resultSetSeleccionadas!=null){
                    categoriasSeleccionadas = new ArrayList<>();
                    while(resultSetSeleccionadas.next()){
                        for (int i =0; i<lvCategoriasModificar.getAdapter().getCount();i++){
                            if(lvCategoriasModificar.getItemAtPosition(i).toString().equals(resultSetSeleccionadas.getString("nombre_categoria"))){
                                lvCategoriasModificar.setItemChecked(i,true);
                                categoriasSeleccionadas.add(lvCategoriasModificar.getItemAtPosition(i).toString());
                            }
                        }
                    }
                }
            }
        } catch (SQLException | ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView lvMultiple = findViewById(R.id.lvCategoriasModificar);
        categoriasSeleccionadas = new ArrayList<>();
        SparseBooleanArray checked = lvMultiple.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            if (checked.valueAt(i)) {
                categoriasSeleccionadas.add(parent.getItemAtPosition(checked.keyAt(i)).toString());
            }
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
        intent.putExtra(Utils.INTENTS_EMAIL,emailUsuario);
        setResult(PopupLugarActivity.POPUPLUGAR_ACTIVITY_CODE, intent);
        super.onBackPressed();
    }
}
