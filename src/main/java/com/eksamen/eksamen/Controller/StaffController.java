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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Controller
public class StaffController {
  ArrayList locations = new ArrayList();

  @GetMapping("/medarbejderliste")
  public String employeeList(Model model) throws SQLException {
    model.addAttribute("employee", new Staff());
    model.addAttribute("location", new Location());

    ResultSet resultSet = DatabaseHandler.getInstance().selectAll("location");

    while (resultSet.next()) {
      locations.add(new Location(resultSet.getInt("location_id"), resultSet.getString("location_name")));
    }

    System.out.println(locations);
    model.addAttribute("locations", locations);

    return "employeeList";
  }

  @PostMapping("/medarbejderliste")
  public String addEmployee(@ModelAttribute Staff staff) {
    try {
      String[] columns = {"firstname", "lastname", "password", "phone", "email", "fk_staff_niveau_id"};
      ArrayList staffArrayList = new ArrayList<>();
      staffArrayList.add(staff.getFirstName());
      staffArrayList.add(staff.getLastName());
      staffArrayList.add(new BCryptPasswordEncoder().encode(staff.getEmail() + staff.getPassword()));
      staffArrayList.add(staff.getPhonenumber());
      staffArrayList.add(staff.getEmail());
      staffArrayList.add(staff.getStaffNiveau());

      DatabaseHandler.getInstance().insert("staff", columns, staffArrayList);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "redirect:/medarbejderliste";
  }
}
