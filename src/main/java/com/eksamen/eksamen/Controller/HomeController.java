package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Base.Session;
import com.eksamen.eksamen.Base.Shift;
import com.eksamen.eksamen.Handler.DatabaseHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {
        //Tjekker om man er logget ind
        if(!Session.isLoggedIn()) return "redirect:/login";

        model.addAttribute("weekNames", getWeekNames());
        model.addAttribute("days", getDays());
        model.addAttribute("months", getMonths());
        model.addAttribute("myShifts", getMyShifts());
        model.addAttribute("freeShifts", getFreeShifts());
        model.addAttribute("isAdmin", Session.isAdmin());
        model.addAttribute("isWorker", Session.isWorker());
        model.addAttribute("isLeader", Session.isLeader());
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

    private ArrayList<Shift>[] getMyShifts() {
        Calendar calendar = Calendar.getInstance();

        String start = String.valueOf(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" 00:00:00");
        calendar.add(Calendar.DATE, 7);
        String end = String.valueOf(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" 23:59:59");
        calendar.add(Calendar.DATE, -7);

        ArrayList<Shift>[] returnMyShifts = (ArrayList<Shift>[])new ArrayList[7];

        String sql = "SELECT shift_id,comment,start_time,end_time,location_name,firstname,lastname,staff_id,approved_by " +
                "FROM shift " +
                "INNER JOIN location ON location_id = fk_location_id " +
                "INNER JOIN shift_request ON fk_shift_id = shift_id " +
                "INNER JOIN staff ON staff_id = fk_staff_id " +
                "WHERE staff_id = '"+ Session.getId()+"' " +
                "AND start_time BETWEEN '"+start+"' AND '"+end+"'";
        //System.out.println(sql);
        ResultSet resultSet = DatabaseHandler.getInstance().querySelect(sql);

        for(int i = 0; i < returnMyShifts.length; i++) {
            returnMyShifts[i] = new ArrayList<Shift>();
            try {
                while (resultSet.next()) {
                    if (String.valueOf(resultSet.getDate("start_time")).equals(String.valueOf(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1 < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : calendar.get(Calendar.MONTH) + 1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH))))) {
                        returnMyShifts[i].add(new Shift(
                                resultSet.getInt("shift_id"),
                                resultSet.getString("comment"),
                                resultSet.getInt("approved_by"),
                                String.valueOf(resultSet.getDate("start_time")),
                                String.valueOf(resultSet.getTime("start_time")),
                                String.valueOf(resultSet.getDate("end_time")),
                                String.valueOf(resultSet.getTime("end_time")),
                                resultSet.getString("location_name"),
                                resultSet.getInt("staff_id"),
                                resultSet.getString("firstname") + " " + resultSet.getString("lastname")));
                    }
                    System.out.println();
                }

                calendar.add(Calendar.DATE, 1);
                resultSet.first();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return returnMyShifts;
    }

    private ArrayList<Shift>[] getFreeShifts() {
        Calendar calendar = Calendar.getInstance();

        String start = String.valueOf(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" 00:00:00");
        calendar.add(Calendar.DATE, 7);
        String end = String.valueOf(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" 23:59:59");
        calendar.add(Calendar.DATE, -7);

        ArrayList<Shift>[] returnFreeShifts = (ArrayList<Shift>[])new ArrayList[7];

        String sql = "SELECT shift_id,comment,start_time,end_time,location_name,approved_by " +
                "FROM shift " +
                "INNER JOIN location ON location_id = fk_location_id " +
                "LEFT JOIN shift_request ON fk_shift_id = shift_id " +
                "AND start_time BETWEEN '"+start+"' AND '"+end+"' AND chosen != 1";
        ResultSet resultSet = DatabaseHandler.getInstance().querySelect(sql);

        for(int i = 0; i < returnFreeShifts.length; i++) {
            returnFreeShifts[i] = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    if (String.valueOf(resultSet.getDate("start_time")).equals(String.valueOf(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1 < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : calendar.get(Calendar.MONTH) + 1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH))))) {
                        returnFreeShifts[i].add(new Shift(
                                resultSet.getInt("shift_id"),
                                resultSet.getString("comment"),
                                0,
                                String.valueOf(resultSet.getDate("start_time")),
                                String.valueOf(resultSet.getTime("start_time")),
                                String.valueOf(resultSet.getDate("end_time")),
                                String.valueOf(resultSet.getTime("end_time")),
                                resultSet.getString("location_name"),
                                0,
                                ""));
                    }
                }

                calendar.add(Calendar.DATE, 1);
                resultSet.first();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return returnFreeShifts;
    }
}
