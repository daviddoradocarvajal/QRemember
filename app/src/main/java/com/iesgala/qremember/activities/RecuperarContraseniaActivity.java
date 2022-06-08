package com.iesgala.qremember.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.RecuperarContraseniaController;
import com.iesgala.qremember.model.Usuario;
import com.iesgala.qremember.utils.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * @author David Dorado
 * @version 1.0
 */
public class RecuperarContraseniaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperarcontrasenia);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Recuperar contraseña");
        TextView tvEmailRecuperar = findViewById(R.id.tvEmailRecuperar);
        // Cambiar preguntas por string array
        Spinner spPreguntasRecuperar = findViewById(R.id.spPreguntaRecuperar);
        ArrayAdapter<CharSequence> spPreguntasAdapter = ArrayAdapter.createFromResource(this,R.array.Preguntas, android.R.layout.simple_spinner_item);
        spPreguntasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPreguntasRecuperar.setAdapter(spPreguntasAdapter);
        TextView tvRespuestaRecuperar = findViewById(R.id.tvRespuestaRecuperar);
        Button btnComprobar = findViewById(R.id.btnComprobar);
        btnComprobar.setOnClickListener(l -> {
            Usuario usuario = new Usuario(tvEmailRecuperar.getText().toString(), "");
            usuario.setPregunta(spPreguntasRecuperar.getSelectedItem().toString());
            usuario.setRespuesta(tvRespuestaRecuperar.getText().toString());
            try {
                if (new checkPregunta().execute(usuario).get().equals("Correcto")){
                 RecuperarContraseniaController.recuperarContrasenia(usuario.getEmail(),this);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private class checkPregunta extends AsyncTask<Usuario, Void, String> {
        Connection conn;

        @Override
        protected String doInBackground(Usuario... usuarios) {
            try {
                if (conn == null)
                    conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT pregunta_seguridad,respuesta FROM Usuario WHERE email='" + usuarios[0].getEmail() + "'");
                if (resultSet.next()) {
                    if (resultSet.getString("pregunta_seguridad").trim().equals(usuarios[0].getPregunta().trim())) {
                        if (resultSet.getString("respuesta").trim().equals(usuarios[0].getRespuesta().trim())) {
                            return "Correcto";
                        } else {
                            return "Respuesta incorrecta";
                        }
                    } else {
                        return "Pregunta de seguridad incorrecta";
                    }
                } else {
                    return "Usuario no encontrado";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error " + e.getMessage();
            } finally {
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return "Error " + e.getMessage();
                }
            }
        }

        @Override
        protected void onPostExecute(String resultado) {
            if (resultado.equals("Correcto")) {
                super.onPostExecute(resultado);
            } else {
                TextView tvFalloRecuperar = findViewById(R.id.tvFalloRecuperar);
                tvFalloRecuperar.setText(resultado);
                tvFalloRecuperar.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(resultado);
        }

    }
}

