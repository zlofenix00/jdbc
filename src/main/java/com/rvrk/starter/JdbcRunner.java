package com.rvrk.starter;

import com.rvrk.starter.util.ConnectionManager;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;

        String sql = """
                CREATE TABLE IF NOT EXISTS info
                (
                                id SERIAL PRIMARY KEY ,
                                data TEXT NOT NULL
                );
                """;

        String sql2 = """
                INSERT INTO info (data)
                VALUES ('test1'),
                ('test2'),
                ('test3'),
                ('test4');
                """;

        String sql3 = """
                UPDATE info
                SET data = 'TestTest'
                WHERE id = 5
                RETURNING *;
                """;

        String sql4 = """
                SELECT *
                FROM ticket
                """;


        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {

            System.out.println(connection.getTransactionIsolation());
            System.out.println(connection.getSchema());

//            int executeResult = statement.executeUpdate(sql3);
            ResultSet query = statement.executeQuery(sql4);

//            System.out.println(executeResult);
//            System.out.println(statement.getUpdateCount());
            while (query.next()){
                System.out.println(query.getLong("id"));
                System.out.println(query.getString("passenger_no"));
                System.out.println(query.getBigDecimal("cost"));
                System.out.println("----------");
            }
        }
    }
}
