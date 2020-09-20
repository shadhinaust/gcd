package com.mohit.gcd;

import com.mohit.gcd.model.Agenda;
import com.mohit.gcd.service.GcdService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GcdServiceTest {
    @Autowired
    private GcdService gcdService;

    @Test
    public void getAllFreeTime(){
        List<Agenda> agendas = new ArrayList<>();
        List<Agenda> freeSlots = gcdService.getAllFreeTime(agendas);
        Assertions.assertThat(freeSlots.size()).isEqualTo(1);
        LocalDateTime dateTime = LocalDateTime.now();
        for(int index = 0; index < 3; index++){
            Agenda agenda = new Agenda();
            dateTime = dateTime.plusMinutes(30);
            agenda.setStart(dateTime);
            dateTime = dateTime.plusMinutes(60);
            agenda.setEnd(dateTime);
            agenda.setSummary("Agenda " + index);
            agendas.add(agenda);
        }
        freeSlots = gcdService.getAllFreeTime(agendas);
        Assertions.assertThat(freeSlots.size()).isEqualTo(4);
    }

}
