package com.example.triviagameproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private Connection conn;

    public DatabaseHandler() {
        try {
            // replace 'database_name', 'username' and 'password' with your actual database name, username and password
            conn = DriverManager.getConnection("jdbc:mysql://localhost/trivia_game", "root", "SQL3nTi2y!*");
        } catch (SQLException e) {
            System.out.println("Couldn't connect to the database");
            e.printStackTrace();
        }
    }

    public void storeUserData(String name, int score) {
        String sql = "INSERT INTO users(name, score) VALUES(?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldn't insert data into the database");
            e.printStackTrace();
        }
    }

    public List<User> getTopScores() {
        String query = "SELECT * FROM users ORDER BY score DESC LIMIT 10";
        List<User> users = new ArrayList<>();

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int score = resultSet.getInt("score");
                users.add(new User(name, score));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }
    }

