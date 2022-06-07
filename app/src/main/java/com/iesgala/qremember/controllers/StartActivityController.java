package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.MainActivity;
import com.iesgala.qremember.activities.RecuperarContraseniaActivity;
import com.iesgala.qremember.activities.RegisterActivity;
import com.iesgala.qremember.model.Usuario;


/**
 * Clase controladora de eventos de StartActivity contiene los métodos a ser ejecutados por los botones
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class StartActivityController {

    public static void accederButton(Usuario usuario, @NonNull Activity activity) {
        TextView tvIncorrecto = activity.findViewById(R.id.tvIncorrectoStart);
        TextView tvEmail = activity.findViewById(R.id.tvUsuario);
        TextView tvPass = activity.findViewById(R.id.tvContrasenia);
        tvIncorrecto.setText("");
        tvIncorrecto.setVisibility(View.GONE);
        if (tvEmail.getText().toString().equals(usuario.getEmail())) {
            if (tvPass.getText().toString().equals(usuario.getContrasenia())) {
                Intent intent = new Intent(activity.getBaseContext(), MainActivity.class);
                intent.putExtra("Nombre", usuario.getNombre());
                intent.putExtra("Email", usuario.getEmail());
                activity.startActivity(intent);
            } else {
                tvIncorrecto.setVisibility(View.VISIBLE);
                tvIncorrecto.setText(R.string.err_contrasenia);
            }
        } else {
            tvIncorrecto.setVisibility(View.VISIBLE);
            tvIncorrecto.setText(R.string.err_usuario);
        }
    }

    public static void registrarButton(@NonNull Activity activity) {
        Intent intent = new Intent(activity.getBaseContext(), RegisterActivity.class);
        activity.startActivity(intent);
    }

    public static void recuperarContrasenia(@NonNull Activity activity) {
        Intent intent = new Intent(activity.getBaseContext(), RecuperarContraseniaActivity.class);
        activity.startActivity(intent);
    }
}


