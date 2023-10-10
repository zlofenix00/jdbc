package com.rvrk.starter;

import com.rvrk.starter.dao.TicketDao;
import com.rvrk.starter.entity.Ticket;

import java.math.BigDecimal;

public class DaoRunner {
    public static void main(String[] args) {
        var ticketDao = TicketDao.getInstance();
        var allTicket = ticketDao.findAll();
        System.out.println(allTicket);
    }

    private static void updateTest() {
        var ticketDao = TicketDao.getInstance();
        var maybeTicket = ticketDao.findById(2L);
        System.out.println(maybeTicket);

        maybeTicket.ifPresent(ticket -> {
            ticket.setCost(BigDecimal.valueOf(188.88));
            ticketDao.update(ticket);
            System.out.println(ticketDao.findById(2L));
        });
    }

    private static void deleteTest() {
        var ticketDao = TicketDao.getInstance();
        var deleteResult = ticketDao.delete(56L);
        System.out.println(deleteResult);
    }

    private static void saveTest() {
        var ticketDao = TicketDao.getInstance();
        var ticket = new Ticket();
        ticket.setPassengerName("text");
        ticket.setPassengerNo("1234456");
        ticket.setFlightId(3L);
        ticket.setSeatNo("B3");
        ticket.setCost(BigDecimal.TEN);
        var saveTicket = ticketDao.save(ticket);
        System.out.println(saveTicket);
    }
}
