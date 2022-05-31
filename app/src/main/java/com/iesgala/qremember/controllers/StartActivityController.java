package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.MainActivity;
import com.iesgala.qremember.activities.RegisterActivity;
import com.iesgala.qremember.model.Usuario;

/**
 * Clase controladora de eventos de StartActivity contiene los métodos a ser ejecutados por los botones
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class StartActivityController {

    public static void accederButton (String usuario, String pass, @NonNull Activity activity){
        Usuario usr = new Usuario("Nombre", "admin@qremember.es","pass");
        TextView tvIncorrecto = activity.findViewById(R.id.tvIncorrectoStart);
        tvIncorrecto.setText("");
        tvIncorrecto.setVisibility(View.GONE);
        if (usuario.equals(usr.getEmail())){
            if (pass.equals(usr.getContrasenia())) {
                Intent intent = new Intent(activity.getBaseContext(), MainActivity.class);
                intent.putExtra("Nombre", usuario);
                activity.startActivity(intent);
            }else {
                tvIncorrecto.setVisibility(View.VISIBLE);
                tvIncorrecto.setText(R.string.err_contrasenia);
            }
        }else{
            tvIncorrecto.setVisibility(View.VISIBLE);
            tvIncorrecto.setText(R.string.err_usuario);
        }
    }
    public static void registrarButton (@NonNull Activity activity) {
        Intent intent = new Intent(activity.getBaseContext(), RegisterActivity.class);
        activity.startActivity(intent);
    }
}
