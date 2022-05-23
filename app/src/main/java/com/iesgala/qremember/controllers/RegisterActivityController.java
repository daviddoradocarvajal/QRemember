package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.iesgala.qremember.activities.StartActivity;
import com.iesgala.qremember.model.Usuario;
import com.iesgala.qremember.utils.FakeDb;

public class RegisterActivityController {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void RegistrarUsuario (String nombre, String email, String contrasenia,@NonNull Activity activity){
        FakeDb db = new FakeDb(activity);
        Usuario usr = new Usuario(nombre,email,contrasenia);
        db.usuarios.add(usr);
        if (db.usuarios.stream().filter(u -> u.getEmail().equals(email)).findFirst().isPresent()){
            Intent intent = new Intent(activity.getBaseContext(), StartActivity.class);
            intent.putExtra("Resultado",true);
            activity.startActivity(intent);
        }
        else{
            Log.d("FALLO", "RegistrarUsuario: No se ha encontrado el registro nuevo");
        }

    }
}
