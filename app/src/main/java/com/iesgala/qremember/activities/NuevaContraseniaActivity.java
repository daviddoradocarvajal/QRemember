package com.iesgala.qremember.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iesgala.qremember.R;
import com.iesgala.qremember.utils.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class NuevaContraseniaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevacontrasenia);
        String email = getIntent().getStringExtra("Usuario");
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.recuperando_contrasenia)+email);
        TextView tvNuevaPass = findViewById(R.id.tvNuevaPass);
        TextView tvNuevaPassRe = findViewById(R.id.tvNuevaPassRe);
        TextView tvInfoNuevaPass = findViewById(R.id.tvInfoNuevaPass);
        TextView tvErrorNuevaPass = findViewById(R.id.tvErrorNuevaPass);
        tvInfoNuevaPass.setText(getString(R.string.recuperando_contrasenia)+" "+email);
        Button btnConfirmarNuevaPass = findViewById(R.id.btnConfirmarNuevaPass);
        btnConfirmarNuevaPass.setOnClickListener(i -> {
            tvErrorNuevaPass.setText("");
            if (tvNuevaPass.getText().toString().trim().equals(tvNuevaPassRe.getText().toString().trim())){
                try {
                    if(new UpdateTask().execute(email,tvNuevaPass.getText().toString()).get()) {
                        Intent intent = new Intent(this.getBaseContext(), StartActivity.class);
                        this.startActivity(intent);
                        this.finish();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                tvErrorNuevaPass.setText(R.string.no_coinciden);
                tvErrorNuevaPass.setVisibility(View.VISIBLE);
            }
        });
    }


    private class UpdateTask extends AsyncTask<String,Void,Boolean>{
        Connection conn;

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                if (conn == null)
                    conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                Statement statement = conn.createStatement();
                statement.execute("UPDATE Usuario SET contrasenia = aes_encrypt('"+strings[1]+"','hunter1') WHERE email='"+strings[0]+"'");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
    }
}
