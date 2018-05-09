package com.eksamen.eksamen.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Calendar;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("weekNames", getWeekNames());
        model.addAttribute("days", getDays());
        model.addAttribute("months", getMonths());
        return "/index";
    }


    private String[] getWeekNames() {
        Calendar calendar = Calendar.getInstance();

        String[] weekNames = {"Søndag","Mandag","Tirdag","Onsdag","Tordag","Fredag","Lørdag"};
        String[] returnNames = new String[7];

        for(int i = 0; i < weekNames.length; i++) {
            returnNames[i] = weekNames[calendar.get(Calendar.DAY_OF_WEEK)-1];
            calendar.add(Calendar.DATE, 1);
        }

        return returnNames;
    }

    private int[] getDays() {
        Calendar calendar = Calendar.getInstance();

        int[] days = new int[7];

        for(int i = 0; i < days.length; i++) {
            days[i] = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DATE, 1);
        }

        return days;
    }

    private String[] getMonths() {
        Calendar calendar = Calendar.getInstance();

        String[] months = {"jan","feb","mar","apr","maj","jun","jul","aug","sep","okt","nov","dec"};
        String[] returnMonths = new String[7];

        for(int i = 0; i < returnMonths.length; i++) {
            returnMonths[i] = months[calendar.get(Calendar.MONTH)];
            calendar.add(Calendar.DATE, 1);
        }

        return returnMonths;
    }
}
