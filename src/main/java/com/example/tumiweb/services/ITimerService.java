package com.example.tumiweb.services;

import java.text.ParseException;
import java.util.Date;

public interface ITimerService {
    long getTimeByDiaryId(Long id) throws ParseException;
    Date getTimeByString(String s) throws ParseException;
    int getMinutesByTime(long time);
    String getDay();
    int getTotalMinuteFromDay(Long id, String day) throws ParseException;
    int getTotalMinuteFromMonth(Long id, int month) throws ParseException;
}
