package com.iesgala.qremember.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.iesgala.qremember.model.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Consulta extends AsyncTask<ArrayList<Usuario>,Void,ArrayList<Usuario>>{

    Connection conexionMySQL = null;
    private Statement st = null;
    private ResultSet rs = null;


    @Override
    protected ArrayList<Usuario> doInBackground(ArrayList<Usuario>... datos)
    {

        String sql = "Select * from Categoria";

        String SERVIDOR = "10.0.0.2";
        String PUERTO = "3306";
        String BD = "db";
        String USUARIO = "usuario";
        String PASSWORD = "pass";

        try{
            Log.d("Conectando","Entrando");
            /*Establecemos la conexión con el Servidor MySQL indicándole la cadena de conexión formada por la dirección ip,
            puerto del servidor, la Base de Datos a la que vamos a conectarnos, y el usuario y contraseña de acceso al servidor.*/
            conexionMySQL = DriverManager.getConnection("jdbc:mysql://" + SERVIDOR + ":" + PUERTO + "/" + BD,
                    USUARIO,PASSWORD);
            if (conexionMySQL != null) Log.d("Conectando","Conectado");
            if (conexionMySQL == null) Log.d("Conectando","FALLO");
            st = conexionMySQL.createStatement();
            rs = st.executeQuery(sql);
            /*Comprobamos que el cursor esté situado en la primera fila.*/
            if(rs.first())
            {
                do
                {
                    System.out.println(rs.getString(1)+" "+rs.getString(2));
                }while(rs.next());
            }

        }catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
            Log.d("Consulta citas", ex.getMessage());
        }

        finally
        {
            try
            {
                st.close();
                rs.close();
                conexionMySQL.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
}
