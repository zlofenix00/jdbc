package com.rvrk.starter;

import com.rvrk.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRunner {
    public static void main(String[] args) throws SQLException {

        long flightId = 8;
        var deleteFlightSql = "DELETE FROM flight WHERE id = ?";
        var deleteTicketsSql = "DELETE FROM ticket WHERE flight_id = ?";

        Connection connection = null;
        PreparedStatement deleteFlightStatement = null;
        PreparedStatement deleteTicketStatement = null;

        try {
            connection = ConnectionManager.open();
            deleteFlightStatement = connection.prepareStatement(deleteFlightSql);
            deleteTicketStatement = connection.prepareStatement(deleteTicketsSql);

            connection.setAutoCommit(false);

            deleteFlightStatement.setLong(1, flightId);
            deleteTicketStatement.setLong(1, flightId);

            deleteTicketStatement.executeUpdate();
            if(true){
                throw new RuntimeException("Ooops");
            }
            deleteFlightStatement.executeUpdate();
            connection.commit();
        } catch (Exception e){
            if (connection != null){
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null){
                connection.close();
            }
            if (deleteFlightStatement != null){
                deleteFlightStatement.close();
            }
            if (deleteTicketStatement != null){
                deleteTicketStatement.close();
            }
        }
    }
}
