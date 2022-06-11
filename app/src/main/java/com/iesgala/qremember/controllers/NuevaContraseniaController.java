package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.NuevaContraseniaActivity;
import com.iesgala.qremember.activities.StartActivity;
import com.iesgala.qremember.utils.AsyncTasks;
import com.iesgala.qremember.utils.Utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author David Dorado
 * @version 1.0
 */
public class NuevaContraseniaController {

    public static boolean actualizarContrasenia(Activity activity, String email) {
        TextView tvNuevaPass = activity.findViewById(R.id.tvNuevaPass);
        TextView tvNuevaPassRe = activity.findViewById(R.id.tvNuevaPassRe);
        if (!tvNuevaPass.getText().toString().matches("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{6,16}$")) {
            Utils.AlertDialogGenerate(tvNuevaPass.getContext(), activity.getString(R.string.err), activity.getString(R.string.err_pass_format));
            return false;
        }
        if (tvNuevaPass.getText().toString().trim().equals(tvNuevaPassRe.getText().toString().trim())) {
            try {
                String sql = "UPDATE Usuario SET contrasenia = aes_encrypt('" + tvNuevaPass.getText().toString() + "','hunter1') WHERE email='" + email + "'";
                boolean actualizado = new AsyncTasks.UpdateTask().execute(sql).get(1, TimeUnit.MINUTES);
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
