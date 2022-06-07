package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;


import android.widget.TextView;

import androidx.annotation.NonNull;


import com.iesgala.qremember.R;

import com.iesgala.qremember.activities.StartActivity;




public class RegisterActivityController {

    public static void UsuarioRegistrado (@NonNull Activity activity)  {
        TextView tvFormEmail = activity.findViewById(R.id.tvFormEmail);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.register_correct);
        builder.setMessage(R.string.register_ok);
        builder.setPositiveButton(R.string.confirmar, (dialogInterface, i) -> {
            Intent intent = new Intent(activity.getBaseContext(), StartActivity.class);
            intent.putExtra("Email",tvFormEmail.getText().toString());
            activity.startActivity(intent);
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }
}
