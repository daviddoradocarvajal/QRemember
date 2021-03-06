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
import com.iesgala.qremember.controllers.MainActivityController;
import com.iesgala.qremember.controllers.NuevaRutaController;
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
 * Actividad encargada de almacenar una Ruta en la base de datos a través de los datos que introduce
 * el usuario, que son nombre, lugares de la ruta (mínimo 2) y categorias de la ruta (mínimo 1)
 * @author David Dorado
 * @version 1.0
 */
public class NuevaRutaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String emailUsuario;
    ArrayList<String> lugaresSeleccionados;
    ArrayList<String> categoriasSeleccionadasNuevaRuta;
    ArrayList<Lugar> lugaresUsuario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_ruta);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.nueva_ruta);
        Intent intent = getIntent();
        emailUsuario = intent.getStringExtra(Utils.INTENTS_EMAIL);
        System.out.println(emailUsuario);
        TextView tvNombreNuevaRuta = findViewById(R.id.tvNombreNuevaRuta);
        ListView lvLugaresNuevaRuta = findViewById(R.id.lvLugaresNuevaRuta);
        ListView lvCategorasNuevaRuta = findViewById(R.id.lvCategoriasNuevaRuta);
        if (setCategorias(lvCategorasNuevaRuta)) {
            if (setLugares(lvLugaresNuevaRuta)) {
            } else
                Utils.AlertDialogGenerate(this, getString(R.string.err), getString(R.string.err_lugares));
        } else
            Utils.AlertDialogGenerate(this, getString(R.string.err), getString(R.string.err_categorias));

        Button btnNuevaRutaConfirmar = findViewById(R.id.btnNuevaRutaConfirmar);
        btnNuevaRutaConfirmar.setOnClickListener(l -> {
            // Si hay 2 o mas lugares marcados se preparan los lugares a insertar
            if (lvLugaresNuevaRuta.getCheckedItemCount()>=2) {
                ArrayList<Lugar> lugaresInsertar = setLugaresRuta();
                // Si el texto del nombre no esta en blanco y hay 1 o mas categorias marcadas se inserta en la base de datos
                if (!tvNombreNuevaRuta.getText().toString().isEmpty()) {
                    if (lvCategorasNuevaRuta.getCheckedItemCount()>=1) {
                        NuevaRutaController.nuevaRuta(this, emailUsuario, tvNombreNuevaRuta.getText().toString(), lugaresInsertar, categoriasSeleccionadasNuevaRuta);
                    } else
                        Utils.AlertDialogGenerate(this, getString(R.string.msg_aviso), getString(R.string.aviso_selecciona_categoria));
                } else
                    Utils.AlertDialogGenerate(this, getString(R.string.msg_aviso), getString(R.string.aviso_nombre));
            } else
                Utils.AlertDialogGenerate(this, getString(R.string.msg_aviso), getString(R.string.aviso_nueva_ruta));
        });
    }

    /**
     * Método engargado de devolver un ArrayList con los lugares seleccionados en el ListView de los
     * lugares
     * @return Una lista con los lugares seleccionados por el usuario
     */
    private ArrayList<Lugar> setLugaresRuta(){
        ArrayList<Lugar> lugaresRetorno = new ArrayList<>();
        for(int i=0;i<lugaresSeleccionados.size();i++){
            for (int j=0;j<lugaresUsuario.size();j++){
                if(lugaresSeleccionados.get(i).equals(lugaresUsuario.get(j).getNombre())) lugaresRetorno.add(lugaresUsuario.get(j));
            }
        }
        return lugaresRetorno;
    }

    /**
     * Método que obtiene los lugares almacenados en la cuenta del usuario y los asigna al ListView
     * introducido como parámetro
     * @param lvLugaresNuevaRuta ListView al que asignar los lugares
     * @return true si se ha asigando correctamente al ListView false en caso de error
     */
    private boolean setLugares(ListView lvLugaresNuevaRuta) {
        try {
            lugaresSeleccionados = new ArrayList<>();
            lugaresUsuario = MainActivityController.obtenerLugares(this, emailUsuario);
            for (Lugar l : Objects.requireNonNull(lugaresUsuario)) {
                lugaresSeleccionados.add(l.getNombre());
            }
            lvLugaresNuevaRuta.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            lvLugaresNuevaRuta.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, lugaresSeleccionados));
            lvLugaresNuevaRuta.setOnItemClickListener(this);
            return true;
        } catch (Exception e) {
            Utils.AlertDialogGenerate(this, getString(R.string.err), e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método que obtiene las categorias de la base de datos y las asigna al ListView introducido
     * como parámetro
     * @param listView ListView al que asignar las categorías
     * @return true si se ha asigando correctamente al ListView false en caso de error
     */
    private boolean setCategorias(ListView listView) {
        try {
            ArrayList<String> categorias = new ArrayList<>();
            ResultSet resultSet = new AsyncTasks.SelectTask().execute("SELECT nombre FROM categoria").get(1, TimeUnit.MINUTES);
            if (resultSet != null) {
                while (resultSet.next()) {
                    categorias.add(resultSet.getString("nombre"));
                }
                listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, categorias));
                listView.setOnItemClickListener(((parent, view, position, id) -> {
                    ListView lvMultiple = findViewById(R.id.lvCategoriasNuevaRuta);
                    categoriasSeleccionadasNuevaRuta = new ArrayList<>();
                    SparseBooleanArray checked = lvMultiple.getCheckedItemPositions();
                    for (int i = 0; i < checked.size(); i++) {
                        if (checked.valueAt(i)) {
                            categoriasSeleccionadasNuevaRuta.add(parent.getItemAtPosition(checked.keyAt(i)).toString());
                        }
                    }
                }));
            }
            return true;
        } catch (SQLException | ExecutionException | InterruptedException | TimeoutException e) {
            Utils.AlertDialogGenerate(this, getString(R.string.err), e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView lvMultiple = findViewById(R.id.lvLugaresNuevaRuta);
        lugaresSeleccionados = new ArrayList<>();
        SparseBooleanArray checked = lvMultiple.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            if (checked.valueAt(i)) {
                lugaresSeleccionados.add(parent.getItemAtPosition(checked.keyAt(i)).toString());
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
        intent.putExtra(Utils.INTENTS_EMAIL, emailUsuario);
        super.onBackPressed();
    }
}
