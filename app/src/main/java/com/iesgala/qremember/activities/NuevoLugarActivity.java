package com.iesgala.qremember.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.utils.Utils;
import com.mysql.jdbc.ByteArrayRow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
public class NuevoLugarActivity extends AppCompatActivity implements ListView.OnItemClickListener{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap bm = null;
    byte[] bmData;
    float longitud;
    float latitud;
    float altitud;
    String enlace;
    String nombreLugar;
    EditText tvNombreLugar;
    ArrayList<String> nombresCategoria;
    String emailUsuario;
    //'longitud', 'latitud', 'altitud', 'enlace', 'nombre_cat', 'email_usuario'
    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            bm = imageBitmap;
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,100, blob);


            bmData = blob.toByteArray();

            try {
                new InsertLugarTask().execute("hola").get(1,TimeUnit.MINUTES);
            } catch (ExecutionException | TimeoutException | InterruptedException e) {
                e.printStackTrace();
            }
            bm.recycle();
            bm = null;

            try {
                blob.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){

        ListView lvMultiple=findViewById(R.id.lvCategorias);
        nombresCategoria = new ArrayList<>();
        SparseBooleanArray checked = lvMultiple.getCheckedItemPositions();
        for(int i=0;i<checked.size();i++)
            if(checked.valueAt(i)){
                nombresCategoria.add(adapterView.getItemAtPosition(checked.keyAt(i)).toString());
            }

    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevolugar);
        Intent intent = getIntent();
        longitud = Float.parseFloat(intent.getStringExtra("longitud"));
        latitud = Float.parseFloat(intent.getStringExtra("latitud"));
        altitud = Float.parseFloat(intent.getStringExtra("altitud"));
        enlace = intent.getStringExtra("enlace");
        emailUsuario = intent.getStringExtra("email");
        Objects.requireNonNull(getSupportActionBar()).setTitle("Nuevo lug " + emailUsuario);

        try {
            ArrayList<String> elementos = new SelectCategoriaTask().execute("hola").get(1, TimeUnit.MINUTES);
            TextView tvLocation = findViewById(R.id.tvLocation);
            TextView tvEnlace = findViewById(R.id.tvEnlace);
            tvNombreLugar = findViewById(R.id.tvNombreLugar);
            tvLocation.setText(longitud + " " + latitud + " " + altitud);
            tvEnlace.setText(enlace);
            ListView lv = findViewById(R.id.lvCategorias);
            lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,elementos));
            lv.setOnItemClickListener(this);
            Button btnTomarFoto = findViewById(R.id.btnTomarFoto);
            btnTomarFoto.setOnClickListener(l -> dispatchTakePictureIntent());


        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        /*
        ArrayAdapter<String> adaptador;
        ListView l=(ListView)findViewById(R.id.lvMultiple);
        l.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,elementos);
        l.setAdapter(adaptador);
        l.setOnItemClickListener(this);
        */
    }
    private class SelectCategoriaTask extends AsyncTask<String,Void,ArrayList<String>> {
        Connection conn;
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            try {
                ArrayList<String> categorias = new ArrayList<>();
                if (conn == null)
                    conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT nombre FROM Categoria");
                while (resultSet.next()){
                    categorias.add(resultSet.getString("nombre"));
                }
                return categorias;
            }catch (Exception e){
                e.printStackTrace();
                return new ArrayList<String>();
            }
        }
    }
    private class InsertLugarTask extends AsyncTask<String, Void, Void> {
        Connection conn;

        @Override
        protected Void doInBackground(String... strings) {
            try {
                nombreLugar = tvNombreLugar.getText().toString();
                if (conn == null)
                    conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                Statement statement = conn.createStatement();

                //Statement statement3 = conn.createStatement();
                statement.execute("INSERT INTO Lugar (longitud, latitud, altitud, enlace, nombre, email_usuario) VALUES ("+longitud+", "+latitud+", "+altitud+", '"+enlace+"', '"+nombreLugar+"', '"+emailUsuario+"') ");
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO Imagen ( imagen, longitud, latitud, altitud) VALUES (?,?,?,?)");
                preparedStatement.setBytes(1,bmData);
                preparedStatement.setFloat(2,longitud);
                preparedStatement.setFloat(3,latitud);
                preparedStatement.setFloat(4,altitud);
                preparedStatement.execute();
                //statement.execute("INSERT INTO Imagen ( imagen, longitud, latitud, altitud) VALUES ('"+bmData+"','"+longitud+"', '"+latitud+"', '"+altitud+"')");
                // una vez por cada categoria seleccionada
                for (String categoria:nombresCategoria) {
                    statement.execute("INSERT INTO Lugar_Categoria (longitud, latitud, altitud, nombre_categoria) VALUES ("+longitud+","+latitud+", "+altitud+",'"+categoria+"')");
                }
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
