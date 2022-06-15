package com.iesgala.qremember.utils;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase para realizar las tareas asíncronas con la base de datos *
 * @author David Dorado
 * @version 1.0
 */
public class AsyncTasks {
    private static Connection conn;

    /**
     * Clase encargada de las consultas Select, devuelve un Resulset cuando se llama a su método get
     */
    public static class SelectTask extends AsyncTask<String, Void, ResultSet> {

        /**
         * Método que ejecuta en otro hilo una consulta pasada como primer parámetro
         * a la base de datos y devuelve un resulset
         * @param strings
         * @return
         */
        @Override
        protected ResultSet doInBackground(String... strings) {
            try {
                if (conn == null || conn.isClosed()) {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                Statement statement = conn.createStatement();
                if(statement.isClosed()){
                    statement = conn.createStatement();
                }
                ResultSet resultSet = statement.executeQuery(strings[0]);
                if(resultSet.isClosed()){
                    resultSet = statement.executeQuery(strings[0]);
                    return resultSet;
                }
                return resultSet;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    /**
     * Clase encargada de las consultas Insert, devuelve un booleano cuando se llama a su método get
     * Se puede usar para diferentes consultas, se divide así para mantener la coherencia
     */
    public static class InsertTask extends AsyncTask<String, Void, Boolean> {

        /**
         * Método que ejecuta la sentencia que se le pasa como primer parámetro y devuelve un
         * booleano en función del resultado de la sentencia
         * @param strings admite varios parámetros aunque solo ejecuta el primero
         * @return true si la consulta se ha ejecutado correctamente false si hay algun error
         */
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                if (conn == null || conn.isClosed()) {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }
            try {
                conn.setAutoCommit(false);
                Statement statement = conn.createStatement();
                if(statement.isClosed()){
                    statement = conn.createStatement();
                }

                statement.execute(strings[0]);
                conn.commit();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        }
    }
    /**
     * Clase encargada de las consultas Update, devuelve un booleano cuando se llama a su método get
     * Se puede usar para diferentes consultas, se divide así para mantener la coherencia
     */
    public static class UpdateTask extends AsyncTask<String, Void, Boolean> {
        /**
         * Método que ejecuta la sentencia que se le pasa como primer parámetro y devuelve un
         * booleano en función del resultado de la sentencia
         * @param strings admite varios parámetros aunque solo ejecuta el primero
         * @return true si la consulta se ha ejecutado correctamente false si hay algun error
         */
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                if (conn == null || conn.isClosed()) {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }
            try {
                conn.setAutoCommit(false);
                Statement statement = conn.createStatement();
                if(statement.isClosed()){
                    statement = conn.createStatement();
                }

                statement.execute(strings[0]);
                conn.commit();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        }
    }
    /**
     * Clase encargada de las consultas Delete, devuelve un booleano cuando se llama a su método get
     * Se puede usar para diferentes consultas, se divide así para mantener la coherencia
     */
    public static class DeleteTask extends AsyncTask<String, Void, Boolean> {
        /**
         * Método que ejecuta la sentencia que se le pasa como primer parámetro y devuelve un
         * booleano en función del resultado de la sentencia
         * @param strings admite varios parámetros aunque solo ejecuta el primero
         * @return true si la consulta se ha ejecutado correctamente false si hay algun error
         */
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                if (conn == null || conn.isClosed()) {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }
            try {
                conn.setAutoCommit(false);
                Statement statement = conn.createStatement();
                if(statement.isClosed()){
                    statement = conn.createStatement();
                }

                statement.execute(strings[0]);
                conn.commit();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        }
    }

    /**
     * Clase encargada de ejecutar las consulta de Insert para las imágenes, se usa única y
     * exclusivamente para insertar imágenes en la base de datos porque no era capaz de colocar
     * los bytes en una consulta Insert normal
     */
    public static class PreparedInsertImageTask extends AsyncTask<Object,Void,Boolean> {
        PreparedStatement preparedStatement = null;
        @Override
        protected Boolean doInBackground(Object... objects) {

                try {
                    if (conn == null || conn.isClosed()) {
                        conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return false;
                }

            try{
                conn.setAutoCommit(false);
                byte[] bmData = (byte[]) objects[0];
                String longitud = (String) objects[1];
                String latitud = (String) objects[2];
                String altitud = (String) objects[3];
                String enlace = (String) objects[4];
                preparedStatement = conn.prepareStatement("INSERT INTO imagen ( imagen, longitud, latitud, altitud,enlace) VALUES (?,?,?,?,?)");
                if(preparedStatement.isClosed() || preparedStatement == null) {
                    preparedStatement = conn.prepareStatement("INSERT INTO imagen ( imagen, longitud, latitud, altitud,enlace) VALUES (?,?,?,?,?)");
                }
                preparedStatement.setBytes(1,bmData);
                preparedStatement.setString(2,longitud);
                preparedStatement.setString(3,latitud);
                preparedStatement.setString(4,altitud);
                preparedStatement.setString(5,enlace);
                preparedStatement.execute();
                conn.commit();
            }catch (SQLException e){
                e.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
            return false;
        }
    }
}
