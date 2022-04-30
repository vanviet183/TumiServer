package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.ITimerService;
import com.example.tumiweb.application.utils.DateTimeUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.ParseException;

@RestApiV1
public class TimerController {
  private final ITimerService timerService;

  public TimerController(ITimerService timerService) {
    this.timerService = timerService;
  }

  @GetMapping(UrlConstant.Timer.DATA_TIMER_NOW)
  public ResponseEntity<?> getMinuteByDayNow(@PathVariable("id") Long id) throws ParseException {
    return VsResponseUtil.ok(timerService.getTotalMinuteFromDay(id, DateTimeUtil.getDateTimeNow()));
  }

  @GetMapping(UrlConstant.Timer.DATA_TIMER_MONTH)
  public ResponseEntity<?> getMinuteFromMonthNow(@PathVariable("id") Long id, @PathVariable("month") int month) throws ParseException {
    return VsResponseUtil.ok(timerService.getTotalMinuteFromMonth(id, month));
  }

}
