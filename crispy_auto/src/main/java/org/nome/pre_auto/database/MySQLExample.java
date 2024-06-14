package org.nome.pre_auto.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLExample {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/automaton_db";
        String username = "DaveTheCeo";
        String password = "daveTheCeo2024";

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connected to MySQL database");

            String sql = "SELECT * FROM primary_data";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String state = resultSet.getString("state");
                String alphabet = resultSet.getString("alphabet");
                String start_state = resultSet.getString("start_state");
                String final_state = resultSet.getString("final_state");

                System.out.println(id + ", " + state + ", " + alphabet + ", " + start_state + ", " + final_state);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}

