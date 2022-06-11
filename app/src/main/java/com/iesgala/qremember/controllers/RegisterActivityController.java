package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;


import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.iesgala.qremember.R;

import com.iesgala.qremember.activities.StartActivity;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.util.concurrent.TimeUnit;


/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class RegisterActivityController {

    public static boolean registrarUsuario(Activity activity){
        TextView tvFormNombre = activity.findViewById(R.id.tvFormNombre);
        TextView tvFormEmail = activity.findViewById(R.id.tvFormEmail);
        TextView tvFormPass = activity.findViewById(R.id.tvFormPass);
        Spinner spPreguntas = activity.findViewById(R.id.spPreguntas);
        TextView tvFormRespuesta = activity.findViewById(R.id.tvFormRespuesta);
        if(!tvFormEmail.getText().toString().matches("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$")){
            Utils.AlertDialogGenerate(tvFormEmail.getContext(),activity.getString(R.string.err),activity.getString(R.string.err_email_format));
            return false;
        }
        if(!tvFormPass.getText().toString().matches("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{6,16}$")){
            Utils.AlertDialogGenerate(tvFormPass.getContext(),activity.getString(R.string.err),activity.getString(R.string.err_pass_format));
            return false;
        }
        if(tvFormRespuesta.getText().toString().isEmpty()){
            Utils.AlertDialogGenerate(tvFormPass.getContext(),activity.getString(R.string.err),activity.getString(R.string.err_respuesta_form));
            return false;
        }
        String sql = "INSERT INTO Usuario VALUES ('"+tvFormEmail.getText().toString()+"','"+tvFormNombre.getText().toString()+"',aes_encrypt('"+tvFormPass.getText().toString()+"','hunter1'),'"+spPreguntas.getSelectedItem().toString()+"','"+tvFormRespuesta.getText().toString()+"')";
        try {
            boolean insertado = new AsyncTasks.InsertTask().execute(sql).get(1, TimeUnit.MINUTES);
            if(insertado){
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.register_correct);
                builder.setMessage(R.string.register_ok);
                builder.setPositiveButton(R.string.confirmar, (dialogInterface, i) -> {
                    Intent intent = new Intent(activity.getBaseContext(), StartActivity.class);
                    intent.putExtra("Email",tvFormEmail.getText().toString());
                    activity.startActivity(intent);
                    activity.finish();
                });
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
