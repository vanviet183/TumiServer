package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.services.IDiaryService;
import com.example.tumiweb.application.services.ITimerService;
import com.example.tumiweb.application.utils.DateTimeUtil;
import com.example.tumiweb.domain.entity.Diary;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
public class TimerServiceImp implements ITimerService {
  private final IDiaryService diaryService;

  public TimerServiceImp(IDiaryService diaryService) {
    this.diaryService = diaryService;
  }

  @Override
  public long getTimeByDiaryId(Long id) throws ParseException {
    Diary diary = diaryService.findDiaryById(id);
    if (diary.getEnd() != null) {
      return getTimeByString(diary.getEnd()).getTime() - getTimeByString(diary.getStart()).getTime();
    } else {
      Date date = new Date();
      return date.getTime() - getTimeByString(diary.getStart()).getTime();
    }
  }

  @Override
  public Date getTimeByString(String s) throws ParseException {
    return DateTimeUtil.getDateByString(s);
  }

  @Override
  public int getMinutesByTime(long time) {
    return (int) (time / (60 * 1000) % 60);
  }

  @Override
  public String getDay() {
    return DateTimeUtil.getDateTimeNow();
  }

  @Override
  public int getTotalMinuteFromDay(Long id, String day) throws ParseException {
    List<Diary> diaries = diaryService.findAllByUserIdAndOnDay(id, day);
    if (diaries.size() == 0) {
      return 0;
    }
    int minute = 0;
    for (Diary diary : diaries) {
      minute += getMinutesByTime(getTimeByDiaryId(diary.getId()));
    }
    return minute;
  }

  @Override
  public int getTotalMinuteFromMonth(Long id, int month) throws ParseException {
    List<Diary> diaries = new ArrayList<>(diaryService.findAllByUserId(id));
    int total = 0;
    Set<String> days = new HashSet<>();
    diaries.forEach(item -> {
      if (Integer.parseInt(item.getDay().split("/")[1]) == month) {
        days.add(item.getDay());
      }
    });

    for (String item : days) {
      total += getTotalMinuteFromDay(id, item);
    }
    return total;
  }

}
