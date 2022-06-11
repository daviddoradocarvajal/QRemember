package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContextParams;
import android.content.Intent;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.MainActivity;
import com.iesgala.qremember.activities.RecuperarContraseniaActivity;
import com.iesgala.qremember.activities.RegisterActivity;
import com.iesgala.qremember.model.Usuario;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;


/**
 * Clase controladora de eventos de StartActivity contiene los métodos a ser ejecutados por los botones
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class StartActivityController {

    public static void accederButton(@NonNull Activity activity) {
        try {
            TextView tvEmail = activity.findViewById(R.id.tvUsuario);
            TextView tvPass = activity.findViewById(R.id.tvContrasenia);
            if(!tvEmail.getText().toString().matches("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$")){
                Utils.AlertDialogGenerate(tvEmail.getContext(),activity.getString(R.string.err),"Introduzca un email valido");
                return;
            }
            String sql = "SELECT nombre,email,aes_decrypt(contrasenia,'hunter1') as password FROM Usuario WHERE email='" + tvEmail.getText().toString() + "'";
            ResultSet resultSet = new AsyncTasks.SelectTask().execute(sql).get(1, TimeUnit.MINUTES);
            Usuario usuario;
            if (resultSet.next()) {
                usuario = new Usuario(resultSet.getString("nombre"), resultSet.getString("email"), resultSet.getString("password"));
                if (tvEmail.getText().toString().equals(usuario.getEmail())) {
                    if (tvPass.getText().toString().equals(usuario.getContrasenia())) {
                        Intent intent = new Intent(activity.getBaseContext(), MainActivity.class);
                        intent.putExtra("Nombre", usuario.getNombre());
                        intent.putExtra("Email", usuario.getEmail());
                        activity.startActivity(intent);
                    } else {
                        Utils.AlertDialogGenerate(activity.findViewById(R.id.tvUsuario).getContext(),activity.getString(R.string.err),activity.getString(R.string.err_contrasenia));
                    }
                }
            } else {
                Utils.AlertDialogGenerate(activity.findViewById(R.id.tvUsuario).getContext(),activity.getString(R.string.err),activity.getString(R.string.err_usuario));

            }
        } catch (Exception e) {
            Utils.AlertDialogGenerate(activity.findViewById(R.id.tvUsuario).getContext(),activity.getString(R.string.err),e.getMessage());
            e.printStackTrace();
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


