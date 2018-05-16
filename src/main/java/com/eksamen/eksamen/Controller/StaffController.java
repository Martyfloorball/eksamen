package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Base.Location;
import com.eksamen.eksamen.Base.Session;
import com.eksamen.eksamen.Base.Staff;
import com.eksamen.eksamen.Handler.DatabaseHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Controller
public class StaffController {
  public int getIdForStaff = 0;

  @GetMapping("/medarbejderliste")
  public String employeeList(Model model) {
    model.addAttribute("employee", new Staff());
    model.addAttribute("locations", getLocations());
    model.addAttribute("niveau", Session.getUserniveau());
    return "employeeList";
  }

  /*
  This method saves a new staff employee to database
  */
  @PostMapping(value = "/medarbejderliste", params = "saveEmployee=Opret")
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

    model.addAttribute("locations", getLocations());
    model.addAttribute("employee", getStaff(email));

    return "staffEdit";
  }

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
            "staff_id = "+getIdForStaff
    );


    return "redirect:/medarbejderliste";
  }

  @PostMapping(value = "/medarbejder", params = "deleteEmployee=Slet")
  public String deleteEmployee() {
    System.out.println("slet medarbejder");

    return "redirect:/medarbejderliste";
  }

  public int getEmployeeId(String email) {

    ResultSet resultSet = DatabaseHandler.getInstance().querySelect(
        "SELECT staff_id " +
            "FROM staff " +
            "WHERE " +
            "email = '" + email + "'"
    );

    try {
      resultSet.next();

      getIdForStaff = resultSet.getInt("staff_id");

      resultSet.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return getIdForStaff;
  }

  public Staff getStaff(String email) {
    Staff staffEdit = new Staff();

    getEmployeeId(email);

    ResultSet resultSet = DatabaseHandler.getInstance().querySelect(
        "SELECT firstname, lastname, phone, email, fk_staff_niveau_id " +
            "FROM " +
            "staff " +
            "WHERE " +
            "email = '" + email + "'"
    );

    try {
      resultSet.next();

      staffEdit.setFirstName(resultSet.getString("firstname"));
      staffEdit.setLastName(resultSet.getString("lastname"));
      staffEdit.setPhonenumber(resultSet.getInt("phone"));
      staffEdit.setEmail(resultSet.getString("email"));
      staffEdit.setStaffNiveau(resultSet.getInt("fk_staff_niveau_id"));

      resultSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return staffEdit;
  }

  /*
  Method to get all the locations in the database into an array
  */
  public ArrayList<Location> getLocations() {
    ArrayList<Location> locations = new ArrayList();

    ResultSet resultSet = DatabaseHandler.getInstance().querySelect("select location_id, location_name\n" +
        "from location inner join staff_location l on location.location_id = l.fk_location_id\n" +
        "inner join staff s on l.fk_staff_id = s.staff_id\n" +
        "where staff_id = " + Session.getId());

    try {
      while (resultSet.next()) {
        locations.add(new Location(resultSet.getInt("location_id"), resultSet.getString("location_name")));
      }
      resultSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return locations;
  }
}
