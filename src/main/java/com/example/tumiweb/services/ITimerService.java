package com.example.tumiweb.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface ITimerService {
    long getTimeByDiaryId(Long id) throws ParseException;
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date getTimeByString(String s) throws ParseException;
    int getMinutesByTime(long time);
    String getDay();
    int getTotalMinuteFromDay(Long id, String day) throws ParseException;
    int getTotalMinuteFromMonth(Long id, int month) throws ParseException;
}
