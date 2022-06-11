package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.TextView;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.NuevaContraseniaActivity;
import com.iesgala.qremember.model.Usuario;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class RecuperarContraseniaController {

    public static boolean recuperarContrasenia(Activity activity){
        TextView tvEmailRecuperar = activity.findViewById(R.id.tvEmailRecuperar);
        Spinner spPreguntasRecuperar = activity.findViewById(R.id.spPreguntaRecuperar);
        TextView tvRespuestaRecuperar = activity.findViewById(R.id.tvRespuestaRecuperar);
        try{
            Usuario usuario = new Usuario(tvEmailRecuperar.getText().toString(), "");
            usuario.setPregunta(spPreguntasRecuperar.getSelectedItem().toString());
            usuario.setRespuesta(tvRespuestaRecuperar.getText().toString());
            String sql="SELECT pregunta_seguridad,respuesta FROM usuario WHERE email='" + usuario.getEmail() + "'";
            ResultSet resultSet = new AsyncTasks.SelectTask().execute(sql).get(1, TimeUnit.MINUTES);
            if (resultSet.next()) {
                if (resultSet.getString("pregunta_seguridad").trim().equals(usuario.getPregunta().trim())) {
                    if (resultSet.getString("respuesta").trim().equals(usuario.getRespuesta().trim())) {
                        Intent intent = new Intent(activity.getBaseContext(), NuevaContraseniaActivity.class);
                        intent.putExtra(Utils.INTENTS_EMAIL,tvEmailRecuperar.getText().toString());
                        activity.startActivity(intent);
                        activity.finish();
                        return true;
                    } else {
                        Utils.AlertDialogGenerate(activity,activity.getString(R.string.err),activity.getString(R.string.err_resp_incorrecta));
                        return false;
                    }
                } else {
                    Utils.AlertDialogGenerate(activity,activity.getString(R.string.err),activity.getString(R.string.err_preg_seg_incorrecta));
                    return false;
                }
            } else {
                Utils.AlertDialogGenerate(activity,activity.getString(R.string.err),activity.getString(R.string.err_email_incorrecto));
                return false;
            }
        }catch (Exception e){
            Utils.AlertDialogGenerate(activity,activity.getString(R.string.err),e.getMessage());
            return false;
        }

    }
}
