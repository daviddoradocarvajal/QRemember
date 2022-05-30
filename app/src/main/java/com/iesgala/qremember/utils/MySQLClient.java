package com.iesgala.qremember.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Clase de prueba intentado conectar con el servidor MYSQL (no conecta)
 * @author David Dorado Carvajal
 * @version 1.0
 */
public class MySQLClient {
    private Connection conn;
    //
    public MySQLClient() {
        try{
            Class.forName(Config.MYSQL).newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://" + Config.SERVIDOR + ":" + Config.PUERTO + "/" + Config.BD,Config.USUARIO,Config.PASSWORD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String selectDePrueba(){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("Select * FROM Usuarios");
            while (rs.next()){
                System.out.println(rs.getString(1)+" "+rs.getString(2));
            }
            return "todo ok";
        }catch (Exception e){
            e.printStackTrace();
            return "mal";
        }

    }

    public Connection getConn() {
        return conn;
    }
// TODO constructor que inicia la conexion
    // TODO método que devuelva la conexión iniciada o la inicie si no está iniciada aun
    // TODO métodos para hacer operaciones CRUD
}
