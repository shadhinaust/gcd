package com.mohit.gcd.service;

import com.google.api.services.calendar.Calendar;
import com.mohit.gcd.model.Agenda;

import java.util.List;

public interface GcdService {
    List<Agenda> getAllAgendas(Calendar calendar) throws Exception;
    List<Agenda> getAllFreeTime(List<Agenda> agendas);
}
