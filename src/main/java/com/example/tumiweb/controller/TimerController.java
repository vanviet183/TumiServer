package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.services.ITimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/timer")
public class TimerController extends BaseController<Integer> {
    SimpleDateFormat formatDay = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    private ITimerService timerService;

    @PostMapping("/{id}/now")
    public ResponseEntity<?> getMinuteByDayNow(@PathVariable("id") Long id) throws ParseException {
        return this.resSuccess(timerService.getTotalMinuteFromDay(id, formatDay.format(new Date())));
    }

    @PostMapping("/{id}/{month}")
    public ResponseEntity<?> getMinuteFromMonthNow(
            @PathVariable("id") Long id,
            @PathVariable("month") int month) throws ParseException {
        return this.resSuccess(timerService.getTotalMinuteFromMonth(id, month));
    }


}
