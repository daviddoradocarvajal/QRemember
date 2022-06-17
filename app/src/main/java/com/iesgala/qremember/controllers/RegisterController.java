package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Spinner;
import android.widget.TextView;
import com.iesgala.qremember.R;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;
import java.util.concurrent.TimeUnit;


/**
 * Clase controladora de la actividad RegisterActivity maneja el evento del botón registrar para
 * registrar un nuevo usuario en la base de datos del sistema
 * @author David Dorado
 * @version 1.0
 */
public class RegisterController {
    /**
     * Método que comprueba el formulario de registro y en caso de estar correcto inserta un nuevo
     * usuario en la base de datos
     * @param activity Actividad que lanza el evento
     * @return true si se ha registrado correctamente false en caso de fallo con los datos introducidos
     */
    public static boolean registrarUsuario(Activity activity){
        // Se obtienen las referencias de los controles del formulario de registro
        TextView tvFormNombre = activity.findViewById(R.id.tvFormNombre);
        TextView tvFormEmail = activity.findViewById(R.id.tvFormEmail);
        TextView tvFormPass = activity.findViewById(R.id.tvFormPass);
        Spinner spPreguntas = activity.findViewById(R.id.spPreguntas);
        TextView tvFormRespuesta = activity.findViewById(R.id.tvFormRespuesta);
        // Se comprueba que el email tiene un formato de email valido
        if(!tvFormEmail.getText().toString().matches("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$")){
            Utils.AlertDialogGenerate(tvFormEmail.getContext(),activity.getString(R.string.err),activity.getString(R.string.err_email_format));
            return false;
        }
        // Se comprueba que la contraseña tiene de 6 a 16 caracteres, 1 minúscula, 1 mayúscula y un número
        if(!tvFormPass.getText().toString().matches("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{6,16}$")){
            Utils.AlertDialogGenerate(tvFormPass.getContext(),activity.getString(R.string.err),activity.getString(R.string.err_pass_format));
            return false;
        }
        // Se comprueba que la respuesta de la pregunta de seguridad no está vacia
        if(tvFormRespuesta.getText().toString().isEmpty()){
            Utils.AlertDialogGenerate(tvFormPass.getContext(),activity.getString(R.string.err),activity.getString(R.string.err_respuesta_form));
            return false;
        }
        String sql = "INSERT INTO usuario VALUES ('"+tvFormEmail.getText().toString()+"','"+tvFormNombre.getText().toString()+"',aes_encrypt('"+tvFormPass.getText().toString()+"','"+Utils.ENCRYPT_PASS+"'),'"+spPreguntas.getSelectedItem().toString()+"','"+tvFormRespuesta.getText().toString()+"')";
        try {
            boolean insertado = new AsyncTasks.InsertTask().execute(sql).get(1, TimeUnit.MINUTES);
            if(insertado){
                // Si el usuario se ha registrado correctamente muestra un cuadro de diálogo
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.register_correct);
                builder.setMessage(R.string.register_ok);
                builder.setPositiveButton(R.string.confirmar, (dialogInterface, i) -> activity.finish());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }else {
                Utils.AlertDialogGenerate(activity.findViewById(R.id.tvFormNombre).getContext(),activity.getString(R.string.err),activity.getString(R.string.insert_err));
                return false;
            }
        }catch (Exception e){
            Utils.AlertDialogGenerate(activity.findViewById(R.id.tvFormNombre).getContext(),activity.getString(R.string.err),e.getMessage());
            return false;
        }
    }

}
