package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Base.Staff;
import com.eksamen.eksamen.Handler.DatabaseHandler;
import com.eksamen.eksamen.Handler.EmailHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class StaffController {

  @PostMapping("/getEmployees")
  public ArrayList getEmployeeList() {
    ArrayList<ArrayList<String>> staffs = new ArrayList<>();
    ArrayList<String> columnLabels = new ArrayList<>();

    try {
      ResultSet rs = DatabaseHandler.getInstance().querySelect(" select\n" +
        "firstname,\n" +
        "lastname,\n" +
        "phone,\n" +
        "email,\n" +
        "location_name,\n" +
        "niveau_name\n" +
        "from staff\n" +
        "inner join staff_location l on staff.staff_id = l.fk_staff_id\n" +
        "inner join staff_niveau n on staff.fk_staff_niveau_id = n.staff_niveau_id\n" +
        "inner join location l2 on l.fk_location_id = l2.location_id;");

      for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
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
}
