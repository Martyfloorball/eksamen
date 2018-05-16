package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Base.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Controller
public class ShiftController {
  private Calendar calendar = new GregorianCalendar();

  @GetMapping("/vagtplan")
  public String vagtplan(Model model){
    System.out.println(Arrays.toString(getDates(calendar)));
    System.out.println(Arrays.toString(getDisabled(calendar)));
    model.addAttribute("isAdmin", Session.isAdmin());
    model.addAttribute("isWorker", Session.isWorker());
    model.addAttribute("isLeader", Session.isLeader());

    return "schedule";
  }

  private int[] getDates(Calendar calendar) {
    int[] dates = new int[42];
    int index = 0;

    for(int date: getPreviousMonthDates(calendar)) {
      dates[index] = date;
      index++;
    }

    for(int date: getCurrentMonthDates(calendar)) {
      dates[index] = date;
      index++;
    }

    for(int i = 1; index < 42; i++) {
      dates[index] = i;
      index++;
    }

    return dates;
  }

  private boolean[] getDisabled(Calendar calendar) {
    boolean[] dates = new boolean[42];
    int index = 0;

    for(int date: getPreviousMonthDates(calendar)) {
      dates[index] = true;
      index++;
    }

    for(int date: getCurrentMonthDates(calendar)) {
      dates[index] = false;
      index++;
    }

    while (index < 42) {
      dates[index] = true;
      index++;
    }

    return dates;
  }

  private int[] getPreviousMonthDates(Calendar calendar) {
    calendar.set(Calendar.DAY_OF_MONTH, 1);

    int daysOfPreviousMonth = calendar.get(Calendar.DAY_OF_WEEK) - 2;
    int[] daysToPrint = new int[daysOfPreviousMonth];

    calendar.add(Calendar.DATE, -1);
    int startFrom = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - (daysToPrint.length - 1);

    for(int i = 0; i < daysToPrint.length; i++) {
      daysToPrint[i] = startFrom;
      startFrom++;
    }

    calendar.add(Calendar.DAY_OF_MONTH, 1);

    return daysToPrint;
  }

  private int[] getCurrentMonthDates(Calendar calendar) {
    int[] daysToPrint = new int[calendar.getActualMaximum(Calendar.DAY_OF_MONTH)];

    calendar.set(Calendar.DAY_OF_MONTH, 1);

    for(int i = 0; i < daysToPrint.length; i++) {
      daysToPrint[i] = calendar.get(Calendar.DAY_OF_MONTH);
      calendar.add(Calendar.DAY_OF_MONTH, 1);
    }

    calendar.add(Calendar.DAY_OF_MONTH, -1);

    return daysToPrint;
  }
}
