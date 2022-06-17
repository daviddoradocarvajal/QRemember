package com.iesgala.qremember.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.iesgala.qremember.R;
import com.iesgala.qremember.controllers.StartActivityController;
import com.iesgala.qremember.utils.Utils;
import java.util.Objects;


/**
 * Actividad lanzadora de la aplicación, es la primera que aparece al abrir la aplicación.
 * Se encarga de llamar a las actividades de registrar un usuario, recuperar su contraseña o en el
 * caso de que el usuario esté registrado y recuerde sus datos de inicio de sesión lanza la actividad
 * MainActivity enviandole en un Intent los datos del email del usuario que realizó el inicio de sesion
 * este email se enviará entre todas las actividades para mantener la referencia del usuario que
 * inició sesion
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start);
            // Método para cambiar el titulo de la barra superior
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.bienvenida);
            TextView tvRecuperarPass = findViewById(R.id.tvPulsaAqui);
            tvRecuperarPass.setOnClickListener(e -> StartActivityController.recuperarContrasenia(this));
            Button btnLogin = findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(l -> StartActivityController.accederButton(this));
            Button btnRegistrar = findViewById(R.id.btnRegistrar);
            btnRegistrar.setOnClickListener(l -> StartActivityController.registrarButton(this));
        }catch (Exception e){
            Utils.AlertDialogGenerate(this,getString(R.string.err),e.getMessage());
        }

    }

}

