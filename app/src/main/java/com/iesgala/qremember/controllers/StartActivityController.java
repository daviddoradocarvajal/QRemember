package com.iesgala.qremember.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iesgala.qremember.R;
import com.iesgala.qremember.activities.MainActivity;
import com.iesgala.qremember.activities.RegisterActivity;
import com.iesgala.qremember.model.Usuario;
import com.iesgala.qremember.utils.MySQLSelectTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Clase controladora de eventos de StartActivity contiene los métodos a ser ejecutados por los botones
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class StartActivityController {

    public static void accederButton (String usuario, String pass, @NonNull Activity activity){
        Usuario usr = new Usuario("Nombre", "admin@qremember.es","pass");
        TextView tvIncorrecto = activity.findViewById(R.id.tvIncorrectoStart);
        tvIncorrecto.setText("");
        tvIncorrecto.setVisibility(View.GONE);
        try {
            Usuario usr2 = new MySQLSelectTask(new String[]{"email", "nombre", "aes_decrypt(contrasenia,'hunter1') as password", "pregunta_seguridad", "respuesta"}).execute("selectDePrueba").get();
            System.out.println(usr2.getContrasenia());
        }catch (Exception e){

        }
       /* if (usuario.equals(usr.getEmail())){
            if (pass.equals(usr.getContrasenia())) {
                Intent intent = new Intent(activity.getBaseContext(), MainActivity.class);
                intent.putExtra("Nombre", usuario);
                activity.startActivity(intent);
            }else {
                tvIncorrecto.setVisibility(View.VISIBLE);
                tvIncorrecto.setText(R.string.err_contrasenia);
            }
        }else{
            tvIncorrecto.setVisibility(View.VISIBLE);
            tvIncorrecto.setText(R.string.err_usuario);
        }*/
        Intent intent = new Intent(activity.getBaseContext(), MainActivity.class);
        intent.putExtra("Nombre", usuario);
        activity.startActivity(intent);
    }
    public static void registrarButton (@NonNull Activity activity) {
        Intent intent = new Intent(activity.getBaseContext(), RegisterActivity.class);
        activity.startActivity(intent);
    }
}

class Task extends AsyncTask<Void,Void,Void>{
    public static final String SERVIDOR = "192.168.1.83";
    public static final String PUERTO = "3306";
    public static final String BD = "Qremember";
    public static final String USUARIO = "root";
    public static final String PASSWORD = "rootpass";
    public static final String DRIVER = "src/lib/mysql-connector-java-5.1.49.jar";
    public static final String MYSQL = "com.mysql.cj.jdbc.Driver";

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Connection conn;
            conn = DriverManager.getConnection("jdbc:mysql://192.168.1.83:3308/Qremember","root","rootpass");
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT email,nombre,aes_decrypt(contrasenia,'hunter1') as password,pregunta_seguridad,respuesta FROM Usuario");
            while (resultSet.next()){
                System.out.println(resultSet.getString("email"));
                System.out.println(resultSet.getString("nombre"));
                System.out.println(resultSet.getString("password"));
                System.out.println(resultSet.getString("pregunta_seguridad"));
                System.out.println(resultSet.getString("respuesta"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /*
    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
    }
    */

}
