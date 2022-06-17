package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.StartActivity;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Clase controladora de la actividad NuevaContraseniaActivity maneja el evento de actualizar la
 * contraseña de un usuario el cual se ha comprobado su identidad
 * @author David Dorado
 * @version 1.0
 */
public class NuevaContraseniaController {
    /**
     * Método que responde al evento de actualizar la contraseña del usuario
     * trás comprobar que la contraseña cumple con las normas de 1 mayúscula una minuscula 1 número
     * y entre 6 y 16 carácteres, comprueba que la contraseña es la misma en los 2 campos de texto
     * y si coinciden realiza la actualización en la base de datos
     * @param activity Activiadad que lanza el evento
     * @param email email del usuario cuya contraseña va a ser actualizada
     * @return True si se a actualizado correctamente la contraseña false si hay algún error, ya sea
     * en la actualización de la contraseña, en la comprobación de los campos o en la comprobación
     * de fortaleza de la contraseña
     */
    public static boolean actualizarContrasenia(Activity activity, String email) {
        TextView tvNuevaPass = activity.findViewById(R.id.tvNuevaPass);
        TextView tvNuevaPassRe = activity.findViewById(R.id.tvNuevaPassRe);
        // Se comprueba que se cumple el criterio de seguridad
        if (!tvNuevaPass.getText().toString().matches("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{6,16}$")) {
            Utils.AlertDialogGenerate(tvNuevaPass.getContext(), activity.getString(R.string.err), activity.getString(R.string.err_pass_format));
            return false;
        }
        // Se comprueba que las cadenas introducidas en los 2 campos habilitados para ello coincidan
        if (tvNuevaPass.getText().toString().trim().equals(tvNuevaPassRe.getText().toString().trim())) {
            try {
                String sql = "UPDATE usuario SET contrasenia = aes_encrypt('" + tvNuevaPass.getText().toString().trim() + "','"+Utils.ENCRYPT_PASS+"') WHERE email='" + email + "'";
                boolean actualizado = new AsyncTasks.UpdateTask().execute(sql).get(1, TimeUnit.MINUTES);
                // Si se ha actualizado la contraseña se lanza un intent a StartActivity
                if (actualizado) {
                    Intent intent = new Intent(activity.getBaseContext(), StartActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                    return true;
                }
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
                Utils.AlertDialogGenerate(tvNuevaPass.getContext(), activity.getString(R.string.err), e.getMessage());
                return false;
            }
        } else {
            Utils.AlertDialogGenerate(tvNuevaPass.getContext(), activity.getString(R.string.err), activity.getString(R.string.no_coinciden));
        }
        return false;
    }
}
