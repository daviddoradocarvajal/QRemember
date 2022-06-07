package com.iesgala.qremember.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.StartActivityController;
import com.iesgala.qremember.model.Usuario;
import com.iesgala.qremember.utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start);
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.bienvenida);
            TextView tvUsuario = findViewById(R.id.tvUsuario);
            TextView tvRecuperarPass = findViewById(R.id.tvPulsaAqui);
            tvRecuperarPass.setOnClickListener(e -> StartActivityController.recuperarContrasenia(this));
            Button btnLogin = findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(l -> {
                try {
                    new SelectUsuarioTask().execute(tvUsuario.getText().toString()).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            Button btnRegistrar = findViewById(R.id.btnRegistrar);
            btnRegistrar.setOnClickListener(l -> StartActivityController.registrarButton(this));
        }catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.err);
            builder.setMessage(e.getMessage());
            e.printStackTrace();
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class SelectUsuarioTask extends AsyncTask<String, Void, Usuario> {
        Connection conn = null;

        @Override
        protected Usuario doInBackground(String... strings) {
            try {
                Usuario usuario;
                if (conn == null)
                    conn = DriverManager.getConnection("jdbc:mysql://" + Config.SERVIDOR + ":" + Config.PUERTO + "/" + Config.BD + "", Config.USUARIO, Config.PASSWORD);
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT nombre,email,aes_decrypt(contrasenia,'hunter1') as password FROM Usuario WHERE email='" + strings[0] + "'");
                if (resultSet.next()) {
                    usuario = new Usuario(resultSet.getString("nombre"), resultSet.getString("email"), resultSet.getString("password"));
                    return usuario;
                } else {
                    return null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if(conn!=null)
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            TextView tvIncorrecto = findViewById(R.id.tvIncorrectoStart);
            if (usuario == null) {
                tvIncorrecto.setText(R.string.err_usuario);
                tvIncorrecto.setVisibility(View.VISIBLE);
            } else {
                StartActivityController.accederButton(usuario, (Activity) tvIncorrecto.getContext());
            }
            super.onPostExecute(usuario);
        }
    }
}

