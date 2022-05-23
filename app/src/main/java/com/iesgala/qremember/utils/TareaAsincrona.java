package com.iesgala.qremember.utils;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class TareaAsincrona extends AsyncTask<String, Void, Boolean>{

    private Connection conexionMySQL;

    @Override
    protected Boolean doInBackground(String... datos)
    {
        String SERVIDOR = "10.0.2.2";
        String PUERTO = "3306";
        String BD = "db";
        String USUARIO = "usuario";
        String PASSWORD = "pass";

        boolean estadoConexion = false;
        try{
            /*Establecemos la conexión con el Servidor MySQL indicándole la cadena de conexión formada por la dirección ip,
            puerto del servidor, la Base de Datos a la que vamos a conectarnos, y el usuario y contraseña de acceso al servidor.*/
            conexionMySQL = DriverManager.getConnection("jdbc:mysql://" + SERVIDOR + ":" + PUERTO + "/" + BD,
                    USUARIO,PASSWORD);

            if(!conexionMySQL.isClosed())
            {
                estadoConexion = true;
            }
        }catch(SQLException ex)
        {
            Log.d("Tarea asincrona",ex.getMessage());
        }
        finally
        {
            try
            {
                if(conexionMySQL != null)
                    conexionMySQL.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return estadoConexion;
    }
}
