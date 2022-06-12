package com.iesgala.qremember.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/**
 *
 * @author David Dorado
 * @version 1.0
 */
public class CompartirLugarActivity extends AppCompatActivity {
    String emailUsuario;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // emailUsuario = usuario_emisor
        //usuario_emisor 	latitud 	longitud 	altitud 	enlace 	usuario_receptor
        // Email del que comparte
        // Lugar para compartir
        // Se escribe el usuario, se comprueba si existe, si existe se inserta en lugares compartidos
    }
}
