/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.*;

public class connection {
    
public static Connection connect() {
        String url = "jdbc:mysql://localhost:3306/infoman_pds";
        String username = "root";
        String password = "password";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Successful connection!");
        }catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return connection;
}
}
        