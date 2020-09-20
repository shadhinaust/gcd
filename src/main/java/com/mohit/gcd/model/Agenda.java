package com.mohit.gcd.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Agenda {
    private LocalDateTime start;
    private LocalDateTime end;
    private String time;
    private String summary;

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = ZonedDateTime.of(start, ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = ZonedDateTime.of(end, ZoneId.systemDefault()).toLocalDateTime();
    }

    public String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return start.format(formatter) + " - " + end.format(formatter);
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getDuration() {
        ZoneId zoneId = ZoneId.systemDefault();
        return end.atZone(zoneId).toEpochSecond() - start.atZone(zoneId).toEpochSecond();
    }
}
