package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Handler.DatabaseHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class StaffController {

  @PostMapping("/getEmployees")
  public ArrayList getEmployeeList(){

    try {
      ResultSet rs = DatabaseHandler.getInstance().selectAll("staff");
      while (rs.next())
        System.out.println(rs.getString("firstname"));

    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ArrayList<>(Arrays.asList(1,2,3,4));
  }
}
