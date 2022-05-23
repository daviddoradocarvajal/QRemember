package com.iesgala.qremember.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.RegisterActivityController;

public class RegisterActivity extends Activity {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);
        TextView tvFormNombre = findViewById(R.id.tvFormNombre);
        TextView tvFormEmail = findViewById(R.id.tvFormEmail);
        TextView tvFormPass = findViewById(R.id.tvFormPass);
        btnRegistrarUsuario.setOnClickListener(l -> RegisterActivityController.RegistrarUsuario(tvFormNombre.getText().toString(),tvFormEmail.getText().toString(),tvFormPass.getText().toString(),this));
    }
}
