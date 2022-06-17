package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Toast;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.MainActivity;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Clase controladora de la actividad EliminarCuentaActivity maneja el evento para eliminar la
 * cuenta de un usuario una vez comprobada su identidad a través de su email y contraseña
 * @author David Dorado
 * @version 1.0
 */
public class EliminarCuentaController {
    /**
     * Método que comprueba si las cadenas introducidas como parámetro coinciden con las almacenadas
     * en una base de datos. Si coinciden lanza un cuadro de dialogo para que el usuario confirme
     * o cancele
     * @param activity Actividad que lanza el evento
     * @param email
     * @param pass
     */
    public static void eliminarCuenta(Activity activity, String email, String pass) {
        try {
            ResultSet resultSet = new AsyncTasks.SelectTask().execute("SELECT email, aes_decrypt(contrasenia,'" + Utils.ENCRYPT_PASS + "') as password FROM usuario WHERE email='" + email + "'").get(1, TimeUnit.MINUTES);
            if (resultSet.next()) {
                if (resultSet.getString("password").equals(pass)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(activity.getString(R.string.msg_aviso));
                    builder.setMessage(activity.getString(R.string.seguro_eliminar_cuenta));
                    // Si el usuario pulsa sobre confirmar se elimina el registro de la base de datos
                    builder.setPositiveButton(activity.getString(R.string.confirmar), (dialog, which) -> {
                        try {
                            if (new AsyncTasks.DeleteTask().execute("DELETE FROM usuario WHERE email = '" + email + "'").get(1, TimeUnit.MINUTES)) {
                                Toast.makeText(activity, "La cuenta ha sido eliminada correctamente", Toast.LENGTH_LONG).show();
                                activity.finishAffinity();
                            } else {
                                Utils.AlertDialogGenerate(activity, activity.getString(R.string.msg_aviso), activity.getString(R.string.err_desconocido));
                                activity.finish();
                            }
                        } catch (ExecutionException | InterruptedException | TimeoutException e) {
                            e.printStackTrace();
                            activity.finish();
                        }
                    });
                    /*
                     * Si el usuario pulsa sobre cancelar se lanza un intent a la clase MainActivity
                     * con el email para mantener la sesion del usuario
                     */
                    builder.setNegativeButton(activity.getString(R.string.cancelar), (dialog, which) -> {
                        Intent intent = new Intent(activity.getBaseContext(), MainActivity.class);
                        intent.putExtra(Utils.INTENTS_EMAIL,email);
                        activity.startActivity(intent);
                        activity.finish();
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else
                    Utils.AlertDialogGenerate(activity, activity.getString(R.string.msg_aviso), activity.getString(R.string.err_contrasenia));
            } else
                Utils.AlertDialogGenerate(activity, activity.getString(R.string.msg_aviso), activity.getString(R.string.err_usuario));
        } catch (SQLException | ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
            activity.finish();
        }
    }
}
