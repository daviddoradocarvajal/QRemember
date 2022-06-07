package com.iesgala.qremember.utils;


import android.os.AsyncTask;

import com.iesgala.qremember.model.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.ResultSet;

import java.sql.Statement;

/**
 * Clase de prueba intentado conectar con el servidor MYSQL (no conecta)
 *
 * @author David Dorado Carvajal
 * @version 1.0
 */
// Primer parámetro parametros segundo el progreso actual con el método publishProgress tercero el resultado a devolver
public class MySQLSelectTask extends AsyncTask<String, Void, Usuario> {
    private String selectClause;

    public MySQLSelectTask(String[] args) {
        if (args.length == 0) selectClause = "";
        else {
            selectClause = "";
            for (String arg : args) {
                selectClause += arg + ",";
            }
            selectClause = selectClause.substring(0, selectClause.length() - 1);
        }
    }

    @Override
    protected Usuario doInBackground(String... args) {
        try {

            if (args[0] == "selectDePrueba") this.SqlPrueba2();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Usuario usuario = new Usuario("nombre", "email", "contraseniausr2");
        return usuario;
    }

    @Override
    protected void onPostExecute(Usuario usuario) {
        super.onPostExecute(usuario);
    }

    protected void SqlPrueba2() {
        try {
            Connection conn;
            conn = DriverManager.getConnection("jdbc:mysql://" + Config.SERVIDOR + ":" + Config.PUERTO + "/" + Config.BD + "", Config.USUARIO, Config.PASSWORD);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT " + selectClause + " FROM Usuario");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("email"));
                System.out.println(resultSet.getString("nombre"));
                System.out.println(resultSet.getString("password"));
                System.out.println(resultSet.getString("pregunta_seguridad"));
                System.out.println(resultSet.getString("respuesta"));
            }
        } catch (Exception e) {

        }
    }
}
