package com.iesgala.qremember.utils;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase para realizar las conexiones con la base de datos
 *
 * @author David Dorado
 * @version 1.0
 */
public class AsyncTasks {
    private static Connection conn;

    public static class SelectTask extends AsyncTask<String, Void, ResultSet> {
        private ResultSet resultSet = null;
        private Statement statement = null;

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
                statement = conn.createStatement();
                if(statement.isClosed() || statement == null){
                    statement = conn.createStatement();
                }
                resultSet = statement.executeQuery(strings[0]);
                if(resultSet.isClosed() || resultSet == null){
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

    public static class InsertTask extends AsyncTask<String, Void, Boolean> {
        private Statement statement = null;

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
                statement = conn.createStatement();
                if(statement.isClosed() || statement==null){
                    statement = conn.createStatement();
                }
                statement.execute(strings[0]);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static class UpdateTask extends AsyncTask<String, Void, Boolean> {
        private Statement statement = null;

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
                statement = conn.createStatement();
                if(statement.isClosed() || statement==null){
                    statement = conn.createStatement();
                }
                statement.execute(strings[0]);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static class DeleteTask extends AsyncTask<String, Void, Boolean> {
        private Statement statement = null;

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
                statement = conn.createStatement();
                if(statement.isClosed() || statement==null){
                    statement = conn.createStatement();
                }
                statement.execute(strings[0]);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

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
            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }
            return false;
        }
    }
}
