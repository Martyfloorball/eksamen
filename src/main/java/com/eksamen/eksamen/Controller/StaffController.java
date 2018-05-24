package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Base.Session;
import com.eksamen.eksamen.Base.Staff;
import com.eksamen.eksamen.Handler.DatabaseHandler;
import com.eksamen.eksamen.StaffService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.ArrayList;

@Controller
public class StaffController {

  @GetMapping("/medarbejderliste")
  public String employeeList(Model model) {
    //Tjekker om man er logget ind
    if(!Session.isLoggedIn()) return "redirect:/login";
    if(Session.getUserniveau() == 17) return "redirect:/";

    model.addAttribute("employee", new Staff());
    model.addAttribute("locations", StaffService.getLocations());
    model.addAttribute("isAdmin", Session.isAdmin());
    model.addAttribute("isWorker", Session.isWorker());
    model.addAttribute("isLeader", Session.isLeader());

    return "employeeList";
  }

  //Create new employee
  @GetMapping("/opretmedarbejder")
  public String employee(Model model){
    //Tjekker om man er logget ind
    if(!Session.isLoggedIn()) return "redirect:/login";
    if(Session.getUserniveau() == 17) return "redirect:/";

    model.addAttribute("employee", new Staff());
    model.addAttribute("locations", StaffService.getLocations());
    model.addAttribute("isAdmin", Session.isAdmin());
    model.addAttribute("isWorker", Session.isWorker());
    model.addAttribute("isLeader", Session.isLeader());

    return "createEmployee";
  }

  //This method saves a new staff employee to database
  @PostMapping(value = "/opretmedarbejder", params = "saveEmployee=Opret")
  public String addEmployee(@RequestParam("checkboxes") int[] locationId, @ModelAttribute Staff staff) {
    try {
      //Saving the new staff employee to the database.
      String[] columns = {"firstname", "lastname", "password", "phone", "email", "fk_staff_niveau_id"};
      ArrayList staffArrayList = new ArrayList();
      staffArrayList.add(staff.getFirstName());
      staffArrayList.add(staff.getLastName());
      staffArrayList.add(new BCryptPasswordEncoder().encode(staff.getEmail() + staff.getPassword()));
      staffArrayList.add(staff.getPhonenumber());
      staffArrayList.add(staff.getEmail());
      staffArrayList.add(staff.getStaffNiveau());

      DatabaseHandler.getInstance().insert("staff", columns, staffArrayList);

      //To get the highest staff id which is the newly created staff employee
      ResultSet highestStaffId = DatabaseHandler.getInstance().select(
          "staff",
          "MAX(staff_id)",
          "",
          "",
          0,
          "",
          "");
      highestStaffId.next();

      //Saving the locations to the new staff employee to the database
      for (int i = 0; i < locationId.length; i++) {
        String[] location_columns = {"fk_staff_id", "fk_location_id"};
        ArrayList locationFilter = new ArrayList();
        locationFilter.add(highestStaffId.getInt(1));
        locationFilter.add(locationId[i]);

        DatabaseHandler.getInstance().insert("staff_location", location_columns, locationFilter);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "redirect:/medarbejderliste";
  }

  @GetMapping("/medarbejder")
  public String editStaff(Model model, @RequestParam String email) {
    //Tjekker om man er logget ind
    if(!Session.isLoggedIn()) return "redirect:/login";

    model.addAttribute("employee", StaffService.getStaff(email));
    model.addAttribute("locations", StaffService.getLocations());
    model.addAttribute("isAdmin", Session.isAdmin());
    model.addAttribute("isWorker", Session.isWorker());
    model.addAttribute("isLeader", Session.isLeader());
    model.addAttribute("booleanCheckArray" , StaffService.booleanCheckArray());
    return "staffEdit";
  }

  //Method that edits an employee and saves the changes to the database.
  @PostMapping(value = "/medarbejder", params = "editEmployee=Save")
  public String editEmployee(@RequestParam("checkboxes") int[] locationId, @ModelAttribute Staff staff) {

    DatabaseHandler.getInstance().update(
        "UPDATE staff "+
            "SET "+
            "firstname = '"+staff.getFirstName()+"', "+
            "lastname = '"+staff.getLastName()+"', "+
            "phone = '"+staff.getPhonenumber()+"', "+
            "email = '"+staff.getEmail()+"', "+
            "fk_staff_niveau_id = "+staff.getStaffNiveau()+" "+
            "WHERE "+
            "staff_id = "+ StaffService.getIdForStaff
    );

    //Removing current locations for an employee
    StaffService.deleteForEmployee("staff_location","fk_staff_id");

    //Saving the locations for the employee to the database
    for (int i = 0; i < locationId.length; i++) {
      String[] location_columns = {"fk_staff_id", "fk_location_id"};
      ArrayList locationFilter = new ArrayList();
      locationFilter.add(StaffService.getIdForStaff);
      locationFilter.add(locationId[i]);

      DatabaseHandler.getInstance().insert("staff_location", location_columns, locationFilter);
    }
    return "redirect:/medarbejderliste";
  }

  //Method that deletes an employee from the database.
  @PostMapping(value = "/medarbejder", params = "deleteEmployee=Slet")
  public String deleteEmployee() {
    //Removing current locations for an employee
    StaffService.deleteForEmployee("staff_location","fk_staff_id");

    //Removes shifts for an employee
    StaffService.deleteForEmployee("shift_request","fk_staff_id");

    //Removes employee
    StaffService.deleteForEmployee("staff","staff_id");

    return "redirect:/medarbejderliste";
  }
}
