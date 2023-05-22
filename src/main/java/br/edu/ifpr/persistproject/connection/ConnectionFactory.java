package br.edu.ifpr.persistproject.connection;

import br.edu.ifpr.persistproject.exception.DatabaseIntegrityException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionFactory {

    private static Connection conn;

    // Padrão de projeto singleton
    public static Connection getConnection(){

        try {

            if (conn == null){
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ifpr_store?useSSL=false&serverTimezone=America/Sao_Paulo", "root", "guilherme");
            }

           return conn;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void statementClose(Statement statement){

        try {
            statement.close();
        } catch (SQLException exception){
            throw new DatabaseIntegrityException(exception.getMessage());
        }

    }

    public static void resultSetClose(ResultSet resultSet){

        try {
            resultSet.close();
        } catch (SQLException exception){
            throw new DatabaseIntegrityException(exception.getMessage());
        }

    }

}
