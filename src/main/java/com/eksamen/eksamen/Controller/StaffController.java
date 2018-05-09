package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Base.Staff;
import com.eksamen.eksamen.Handler.DatabaseHandler;
import com.eksamen.eksamen.Handler.EmailHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.Query;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class StaffController {

  /*@GetMapping("/medarbejderliste")
  public String employee(Model model) {
    model.addAttribute("employee", new Staff());
    return "/medarbejderliste";
  }*/

  @PostMapping("/getEmployees")
  public ArrayList getEmployeeList() {
    ArrayList<ArrayList<String>> staffs = new ArrayList<>();
    ArrayList<String> columnLabels = new ArrayList<>();

    try {
      ResultSet rs = DatabaseHandler.getInstance().querySelect(" SELECT\n" +
          "CONCAT(firstname, ' ', lastname) AS Navn,\n" +
          "phone AS Telefonnummer,\n" +
          "email AS Email,\n" +
          "location_name AS Anlæg,\n" +
          "niveau_name AS Stilling\n" +
          "FROM staff\n" +
          "INNER JOIN staff_location l ON staff.staff_id = l.fk_staff_id\n" +
          "INNER JOIN staff_niveau n ON staff.fk_staff_niveau_id = n.staff_niveau_id\n" +
          "INNER JOIN location l2 ON l.fk_location_id = l2.location_id;");

      for (int i = 1; i < rs.getMetaData().getColumnCount() +1; i++) {
        columnLabels.add(rs.getMetaData().getColumnLabel(i));
      }
      staffs.add(columnLabels);

      while (rs.next()) {
        ArrayList<String> staff = new ArrayList<>();

        for (int i = 1; i < columnLabels.size() + 1; i++) {
          staff.add(rs.getString(i));
        }
        staffs.add(staff);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return staffs;
  }

  @PostMapping("/medarbejderliste")
  public String addEmployee(@ModelAttribute Staff staff){
    try {
      String[] columns = {"firstname", "lastname", "password", "phone", "email", "fk_staff_niveau_id"};
      ArrayList staffArrayList = new ArrayList<>();
      staffArrayList.add(staff.getFirstName());
      staffArrayList.add(staff.getLastName());
      staffArrayList.add(new BCryptPasswordEncoder().encode(staff.getEmail()+staff.getPassword()));
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
