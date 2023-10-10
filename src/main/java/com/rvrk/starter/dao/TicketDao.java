package com.rvrk.starter.dao;

import com.rvrk.starter.entity.Ticket;
import com.rvrk.starter.exception.DaoException;
import com.rvrk.starter.util.ConnectionManager;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDao {

    private static final TicketDao INSTANCE = new TicketDao();
    private static final String DELETE_SQL = """
            DELETE FROM ticket
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO ticket(passenger_no, passenger_name, flight_id, seat_on, cost) 
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE ticket
            SET passenger_no = ?,
            passenger_name = ?,
            flight_id = ?,
            seat_on = ?,
            cost = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
            passenger_no,
            passenger_name,
            flight_id,
            seat_on,
            cost
            FROM ticket
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private TicketDao() {
    }

    public List<Ticket> findAll(){
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)
        ) {
            var resultSet = preparedStatement.executeQuery();
            List<Ticket> tickets= new ArrayList<>();
            while (resultSet.next()){
                tickets.add(buildTicket(resultSet));
            }
            return tickets;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Ticket> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Ticket ticket = null;
            if (resultSet.next()) {
                ticket = buildTicket(resultSet);
            }

            return Optional.ofNullable(ticket);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void update(Ticket ticket) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)
        ) {
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlightId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());
            preparedStatement.setLong(6, ticket.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Ticket save(Ticket ticket) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlightId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                ticket.setId(generatedKeys.getLong("id"));
            }
            return ticket;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

    @NotNull
    private static Ticket buildTicket(ResultSet resultSet) throws SQLException {
        return new Ticket(
                resultSet.getLong("id"),
                resultSet.getString("passenger_no"),
                resultSet.getString("passenger_name"),
                resultSet.getLong("flight_id"),
                resultSet.getString("seat_on"),
                resultSet.getBigDecimal("cost")
        );
    }

}
