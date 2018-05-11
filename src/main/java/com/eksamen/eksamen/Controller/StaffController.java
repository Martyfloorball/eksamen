package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Base.Location;
import com.eksamen.eksamen.Base.Staff;
import com.eksamen.eksamen.Handler.DatabaseHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class StaffController {
  ArrayList locations = new ArrayList();

  @GetMapping("/medarbejderliste")
  public String employeeList(Model model) throws SQLException {
    model.addAttribute("employee", new Staff());
    model.addAttribute("locations", locations);

    ResultSet resultSet = DatabaseHandler.getInstance().selectAll("location");

    if (locations.size() == 0) { // for at der ikke kommer duplicates i checkboxes i oprettelse af medarbejder.
      while (resultSet.next()) {
        //der skal tilføjes en if/else der kun skal finde de anlæg centerlederen / admin er en del af.

        locations.add(new Location(resultSet.getInt("location_id"), resultSet.getString("location_name")));
      }
    }

    resultSet.close();

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
      ResultSet highestStaffId = DatabaseHandler.getInstance().select("staff", "MAX(staff_id)", "", "",0,"", "");
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

  /*
  Method to filter the stafflist
   */
  @PostMapping(value = "/medarbejderliste", params = "filterLocations=Filtrér lokationer")
  public String filterStaffLocation(@RequestParam("checkboxesLocation") int[] filterLocationId){



    System.out.println(Arrays.toString(filterLocationId));


    return "redirect:/medarbejderliste";
  }

}
