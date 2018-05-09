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

  @GetMapping("/medarbejderliste")
  public String showEmployeeList(){
    return "/employeeList";
  }

//  @PostMapping("/medarbejderliste")
//  public String addEmployee(@ModelAttribute Staff staff){
//    try {
//      String[] columns = {"firstname", "lastname", "password", "phone", "email", "fk_staff_niveau_id"};
//      ArrayList staffArrayList = new ArrayList<>();
//      staffArrayList.add(staff.getFirstName());
//      staffArrayList.add(staff.getLastName());
//      staffArrayList.add(new BCryptPasswordEncoder().encode(staff.getEmail()+staff.getPassword()));
//      staffArrayList.add(staff.getPhonenumber());
//      staffArrayList.add(staff.getEmail());
//      staffArrayList.add(staff.getStaffNiveau());
//
//      DatabaseHandler.getInstance().insert("staff", columns, staffArrayList);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//
//
//    return "redirect:/medarbejderliste";
//  }
}
