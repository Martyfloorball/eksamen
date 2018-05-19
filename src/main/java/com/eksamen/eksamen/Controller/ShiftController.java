package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Base.Session;
import com.eksamen.eksamen.Base.Shift;
import com.eksamen.eksamen.Base.Staff;
import com.eksamen.eksamen.Handler.DatabaseHandler;
import com.eksamen.eksamen.StaffService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Controller
public class ShiftController {
  private Calendar calendar = new GregorianCalendar();
  private boolean isMonthShown = true;
  private String[] months = {"Jan", "Feb", "Mar", "Apr", "Maj", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"};

  //Henter formular for opret ny vagt
  @GetMapping("/createShift")
  public String createNewShift(Model model){
    model.addAttribute("isAdmin", Session.isAdmin());
    model.addAttribute("isWorker", Session.isWorker());
    model.addAttribute("isLeader", Session.isLeader());
    //Henter anlæg i databasen og sender med til HTML
    model.addAttribute("locations", StaffService.getLocations());
    //Henter medarbejdere og sender med til HTML
    model.addAttribute("staffs", getStaff());
    //Opretter shift objekt til data fra opret vagt
    model.addAttribute("shift", new Shift());
    return"/createShift";
  }

  //Gemmer vagt i databasen
  @PostMapping(value = "/createShift")
  public String addShift(@RequestParam int location, @RequestParam int staffId, @ModelAttribute Shift shift){
    try{
      //Hvis der er valgt medarbejder, sættes vagten som godkendt, af den person der er logget ind
      if(shift.getStaffId() != 0){
        shift.setApproved(Session.getId());
      }
      //Hvis der ikke er indtastet slutdato, sættes den til startdato
      if(shift.getEnd_date().equals("")){
        shift.setEnd_date(shift.getStart_date());
      }
      //Data indsættes i arraylist, og overføres til databasen
      DatabaseHandler.getInstance().insert(
              "shift",
              new String[]{"comment", "approved_by", "start_time", "end_time", "fk_location_id"},
              new ArrayList(Arrays.asList(
                      shift.getComment(),
                      shift.getApproved(),
                      shift.getStart_date() + " " + shift.getStart_time() + ":00",
                      shift.getEnd_date() + " " + shift.getEnd_time() + ":00",
                      location)));
      //Hvis der er valgt medarbejder, indsættes data i arraylist og overføres til databasen
      if(staffId != 0){
        ResultSet resultSet = DatabaseHandler.getInstance().querySelect("select shift_id from shift order by shift_id desc limit 1");
        resultSet.next();
        DatabaseHandler.getInstance().insert(
                "shift_request",
                new String[]{"chosen", "fk_staff_id", "fk_shift_id"},
                new ArrayList(Arrays.asList(1, staffId, resultSet.getInt("shift_id"))));
      }
    }catch(Exception e) {
      e.printStackTrace();
    }
    //Returnerer til vagtplanen, når vagten er oprettet
    return "redirect:/vagtplan";
  }

  @RequestMapping("/vagtplan/{action}")
  public String change(@PathVariable("action") String action) {
    System.out.println(action);
    if (action.equals("next")) {
      if (isMonthShown) {
        calendar.add(Calendar.MONTH, 1);
      } else {
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
      }
    } else if(action.equals("prev")) {
      if (isMonthShown) {
        calendar.add(Calendar.MONTH, -1);
      } else {
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
      }
    }
    return "redirect:/vagtplan";
  }

  @RequestMapping("/vagtplan")
  public String vagtplan(Model model){
    System.out.println(Arrays.toString(getDates(calendar)));
    model.addAttribute("dates", getDates(calendar));
    model.addAttribute("isDisabled", getDisabled(calendar));
    model.addAttribute("month_and_year", months[calendar.get(Calendar.MONTH)]+" "+calendar.get(Calendar.YEAR));
    model.addAttribute("isAdmin", Session.isAdmin());
    model.addAttribute("isWorker", Session.isWorker());
    model.addAttribute("isLeader", Session.isLeader());
    model.addAttribute("shifts", getShifts(calendar));
    model.addAttribute("myID", Session.getId());
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

    int daysOfPreviousMonth = (calendar.get(Calendar.DAY_OF_WEEK) == 1 ? 6 : calendar.get(Calendar.DAY_OF_WEEK) - 2);
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

  private ArrayList<Shift>[] getShifts(Calendar calendar) {
    ArrayList<Shift>[] returnArray = new ArrayList[42];

    //Gå en måned tilbage
    calendar.add(Calendar.MONTH, -1);

    //Find den sidste dato
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

    //Gå tilbage til den sidste mandag
    calendar.add(Calendar.DATE, -(calendar.get(Calendar.DAY_OF_WEEK) == 1 ? 6 : calendar.get(Calendar.DAY_OF_WEEK) - 2));

    for(int i = 0; i < 42; i++) {
      String date;

      returnArray[i] = new ArrayList<>();

      //Egne vagter
      date = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
      ResultSet resultSet = DatabaseHandler.getInstance().querySelect(
              "SELECT * FROM shift INNER JOIN location ON fk_location_id = location_id INNER JOIN shift_request ON fk_shift_id = shift_id INNER JOIN staff ON fk_staff_id = staff_id WHERE chosen = '1' AND start_time >= '"+date+" 00:00:00' AND end_time <= '"+date+" 23:59:59' AND staff_id = '"+Session.getId()+"'"
      );
      addShift(i, resultSet, returnArray);

      //Andre vagter
      date = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
      ResultSet resultSet2 = DatabaseHandler.getInstance().querySelect(
              "SELECT * FROM shift INNER JOIN location ON fk_location_id = location_id INNER JOIN shift_request ON fk_shift_id = shift_id INNER JOIN staff ON fk_staff_id = staff_id WHERE chosen != '1' AND start_time >= '"+date+" 00:00:00' AND end_time <= '"+date+" 23:59:59' AND staff_id != '"+Session.getId()+"'"
      );
      //System.out.println("SELECT * FROM shift INNER JOIN location ON fk_location_id = location_id INNER JOIN shift_request ON fk_shift_id = shift_id INNER JOIN staff ON fk_staff_id = staff_id WHERE chosen != '"+Session.getId()+"' AND start_time >= '"+date+" 00:00:00' AND end_time <= '"+date+" 23:59:59' AND staff_id != '"+Session.getId()+"'");
      addShift(i, resultSet2, returnArray);

      //Ledige vagter

      //Til næste dag
      calendar.add(Calendar.DATE, 1);
    }

    calendar.add(Calendar.MONTH, -1);

    System.out.println(Arrays.toString(returnArray));
    return returnArray;
  }

  private void addShift(int i, ResultSet resultSet, ArrayList<Shift>[] returnArray) {
    try {
      while(resultSet.next()) {
        returnArray[i].add(new Shift(
                resultSet.getInt("shift_id"),
                resultSet.getString("comment"),
                resultSet.getInt("approved_by"),
                String.valueOf(resultSet.getDate("start_time")),
                String.valueOf(resultSet.getTime("start_time")),
                String.valueOf(resultSet.getDate("end_time")),
                String.valueOf(resultSet.getTime("end_time")),
                resultSet.getString("location_name"),
                resultSet.getInt("staff_id"),
                resultSet.getString("firstname")+" "+resultSet.getString("lastname")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  //Henter medarbejdere fra databasen
  public ArrayList<Staff> getStaff(){
    ArrayList<Staff> staffs = new ArrayList<>();

    //Henter id og navn i databasen for alle ansatte
    ResultSet resultSet = DatabaseHandler.getInstance().querySelect("select staff_id, firstname, lastname from staff");

    try {
      //Tilføjer ansatte til arraylist
      while (resultSet.next()) {
        staffs.add(new Staff(resultSet.getInt("staff_id"),
                resultSet.getString("firstname"),
                resultSet.getString("lastname")));
      }
      resultSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return staffs;
  }
}
