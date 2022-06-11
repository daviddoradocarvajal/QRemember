package com.iesgala.qremember.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;

import androidx.appcompat.content.res.AppCompatResources;

import com.iesgala.qremember.R;
import com.iesgala.qremember.model.Categoria;
import com.iesgala.qremember.model.Imagen;
import com.iesgala.qremember.model.Lugar;
import com.iesgala.qremember.model.Ruta;
import com.iesgala.qremember.model.Usuario;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;

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
            if (conn == null) {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return null;
                }
            }
            try {
                statement = conn.createStatement();
                resultSet = statement.executeQuery(strings[0]);
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
            if (conn == null) {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return false;
                }
            }
            try {
                statement = conn.createStatement();
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
            if (conn == null) {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Utils.SERVIDOR + ":" + Utils.PUERTO + "/" + Utils.BD + "", Utils.USUARIO, Utils.PASSWORD);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    return false;
                }
            }
            try {
                statement = conn.createStatement();
                if(statement.isClosed()){
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
}
