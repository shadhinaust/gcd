package com.mohit.gcd.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.mohit.gcd.model.Agenda;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GcdServiceImpl implements GcdService {
    @Override
    public List<Agenda> getAllAgendas(Calendar calendar) {
        List<Agenda> agendas = new ArrayList<>();
        try {
            long now = System.currentTimeMillis();
            DateTime minDate = new DateTime(now);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'z'");
            DateTime maxDate = new DateTime(LocalDateTime.now().toLocalDate().atTime(23,59,59,999).format(formatter));
            Events events = calendar.events()
                    .list("primary")
                    .setTimeMin(minDate)
                    .setTimeMax(maxDate)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();
            if (!items.isEmpty()) {
                for (Event event : items) {
                    Agenda agenda = new Agenda();
                    agenda.setSummary(event.getSummary());
                    LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getStart().getDateTime().getValue()), ZoneId.systemDefault());
                    agenda.setStart(start);
                    LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getEnd().getDateTime().getValue()), ZoneId.systemDefault());
                    agenda.setEnd(end);
                    agendas.add(agenda);
                }
            }
        } catch (Exception exception) {
            agendas = new ArrayList<>();
        }
        List<Agenda> sortedAgendas = agendas.stream()
                .sorted(Comparator.comparingLong(Agenda::getDuration))
                .sorted(Comparator.comparing(Agenda::getStart))
                .collect(Collectors.toList());
        return sortedAgendas;
    }

    @Override
    public List<Agenda> getAllFreeTime(List<Agenda> agendas) {
        List<Agenda> freeSlots = new ArrayList<>();
        LocalDateTime min = LocalDateTime.now();
        LocalDateTime max = min.toLocalDate().atTime(23,59,59,999);
        if(!agendas.isEmpty()){
            for (Agenda agenda: agendas) {
                if(min.isBefore(agenda.getStart())) {
                    Agenda free = new Agenda();
                    free.setStart(min);
                    free.setEnd(agenda.getStart());
                    freeSlots.add(free);
                }
                min = min.isAfter(agenda.getEnd()) ? min : agenda.getEnd();
            }
        }
        if(min.isBefore(max)){
            Agenda free = new Agenda();
            free.setStart(min);
            free.setEnd(max);
            freeSlots.add(free);
        }
        return freeSlots;
    }
}
