package com.rvrk.starter;

import com.rvrk.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlInjectionRunner {
    public static void main(String[] args) throws SQLException {
        String flightId = "2";
        var result = getTicketsByFlightId(flightId);
        System.out.println(result);
    }

    private static List<Long> getTicketsByFlightId(String flightId) throws SQLException {
        String sql = """
                SELECT id
                FROM ticket
                WHERE flight_id = %s
                """.formatted(flightId);

        List<Long> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()
        ) {
            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
//                result.add(resultSet.getLong("id"));
                result.add(resultSet.getObject("id", Long.class)); // NULL safe
            }
        }
        return result;
    }
}
