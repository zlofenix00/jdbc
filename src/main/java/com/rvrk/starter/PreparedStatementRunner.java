package com.rvrk.starter;

import com.rvrk.starter.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PreparedStatementRunner {
    public static void main(String[] args) throws SQLException {
        Long flightId = 2L;
        var result = getTicketsByFlightId(flightId);
        System.out.println(result);

        var flightsBetween = getFlightsBetween(LocalDate
                .of(2020, 10, 1)
                .atStartOfDay(), LocalDateTime.now());
        System.out.println(flightsBetween);
    }

    private static List<Long> getFlightsBetween(LocalDateTime start, LocalDateTime end) throws SQLException {
        String sql = """
                SELECT id
                FROM flight
                WHERE departure_date BETWEEN ? AND ?
                """;

        List<Long> result = new ArrayList<>();

        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sql)) {
            System.out.println(preparedStatement);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(start));
            System.out.println(preparedStatement);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(end));
            System.out.println(preparedStatement);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getObject("id", Long.class));
            }
        }
        return result;

    }

    private static List<Long> getTicketsByFlightId(Long flightId) throws SQLException {
        String sql = """
                SELECT id
                FROM ticket
                WHERE flight_id = ?
                """;

        List<Long> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.open();
             var prepareStatement = connection.prepareStatement(sql)
        ) {
            prepareStatement.setLong(1, flightId);
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getObject("id", Long.class)); // NULL safe
            }
        }
        return result;
    }
}
