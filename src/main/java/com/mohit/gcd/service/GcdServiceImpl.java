package com.mohit.gcd.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.mohit.gcd.model.Agenda;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GcdServiceImpl implements GcdService {
    @Override
    public List<Agenda> getAllAgendas(Calendar calendar) {
        List<Agenda> agendas = new ArrayList<>();
        try {
            Events events = calendar.events()
                    .list("primary")
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();
            if (!items.isEmpty()) {
                for (Event event : items) {
                    Agenda agenda = new Agenda();
                    DateTime start = event.getStart().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    agenda.setTime(start.toString());
                    agenda.setDescription(event.getSummary());
                    agendas.add(agenda);
                }
            }
        } catch (Exception exception) {

        }
        return agendas;
    }
}
