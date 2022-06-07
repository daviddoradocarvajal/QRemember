package com.iesgala.qremember.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.RegisterActivityController;

import com.iesgala.qremember.utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Registrar nuevo usuario");
        Button btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);
        TextView tvFormNombre = findViewById(R.id.tvFormNombre);
        TextView tvFormEmail = findViewById(R.id.tvFormEmail);
        TextView tvFormPass = findViewById(R.id.tvFormPass);
        Spinner spPreguntas = findViewById(R.id.spPreguntas);
        ArrayAdapter<CharSequence> spPreguntasAdapter = ArrayAdapter.createFromResource(this,R.array.Preguntas, android.R.layout.simple_spinner_item);
        spPreguntasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPreguntas.setAdapter(spPreguntasAdapter);
        TextView tvFormRespuesta = findViewById(R.id.tvFormRespuesta);
        btnRegistrarUsuario.setOnClickListener(l ->{
            if(spPreguntas.getSelectedItem() != null && spPreguntas.getSelectedItem().equals(getResources().getStringArray(R.array.Preguntas)[0]) == false)
                new InsertTask().execute(tvFormEmail.getText().toString(),
                        tvFormNombre.getText().toString(),
                        tvFormPass.getText().toString(),
                        spPreguntas.getSelectedItem().toString(),
                        tvFormRespuesta.getText().toString());
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(tvFormNombre.getContext());
                builder.setTitle(R.string.err);
                builder.setMessage(R.string.seleccione_pregunta);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
                }
        );
    }
    private class InsertTask extends AsyncTask<String,Void, Boolean>{
        Connection conn = null;

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                if(strings.length==5){
                    if (conn == null)
                        conn = DriverManager.getConnection("jdbc:mysql://" + Config.SERVIDOR + ":" + Config.PUERTO + "/" + Config.BD + "", Config.USUARIO, Config.PASSWORD);
                    Statement statement = conn.createStatement();
                    statement.execute("INSERT INTO Usuario VALUES ('"+strings[0]+"','"+strings[1]+"',aes_encrypt('"+strings[2]+"','hunter1'),'"+strings[3]+"','"+strings[4]+"')");
                    return true;
                }else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (conn!=null)
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            TextView tvFormNombre = findViewById(R.id.tvFormNombre);
            if (!aBoolean){
                AlertDialog.Builder builder = new AlertDialog.Builder(tvFormNombre.getContext());
                builder.setTitle(R.string.err);
                builder.setMessage(R.string.form_err);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }else {
               RegisterActivityController.UsuarioRegistrado((Activity) tvFormNombre.getContext());
            }
            super.onPostExecute(aBoolean);
        }
    }
}
