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
    private final String SERVIDOR = "192.168.1.210";
    private final String PUERTO = "3308";
    private final String BD = "db";
    private final String USUARIO = "usuario";
    private final String PASSWORD = "pass";
    private Connection conn;
    //
    public MySQLClient() {
        try{
        conn = DriverManager.getConnection("jdbc:mysql://" + SERVIDOR + ":" + PUERTO + "/" + BD,USUARIO,PASSWORD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String selectDePrueba(){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("Select * FROM CATEGORIA");
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
