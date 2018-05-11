package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Handler.DatabaseHandler;
import com.eksamen.eksamen.Base.Session;
import com.eksamen.eksamen.Handler.DatabaseHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.util.ArrayList;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class AjaxController {

  @PostMapping(value = "/getEmployees")
  public ArrayList getEmployeeList() {
    ArrayList<ArrayList<String>> staffs = new ArrayList<>();
    ArrayList<String> columnLabels = new ArrayList<>();

    String filter = "";
    /*if(array.length == 1){
      filter += "WHERE location_id = "; // + array[0] + " ";
    }else if(array.length > 1){
      filter += "WHERE location_id = "; // + array[0]

      /*for (int i = 1; i < arr.length; i++ ){ //skal skippe første plads i array
        filter += " OR location_id = "; //+ array[i] + " ";
      }
    }*/

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
          "INNER JOIN location l2 ON l.fk_location_id = l2.location_id;"); //"+ filter+"

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
  @PostMapping("/getProfile")
  public ArrayList<ArrayList<String>> getUser() {

    ArrayList<ArrayList<String>> user = new ArrayList<>();
    ArrayList<String> temp = new ArrayList<>();
    ArrayList<String> temp2 = new ArrayList<>();
    ResultSet userRS = DatabaseHandler.getInstance()
      .querySelect("select firstname, lastname, email, phone, niveau_name\n" +
        "from staff\n" +
        "inner join staff_niveau n on staff.fk_staff_niveau_id = n.staff_niveau_id\n" +
        "where staff_id = " + Session.getId());


    try {
      userRS.next();
      for (int i = 1; i < userRS.getMetaData().getColumnCount() + 1; i++) {
        temp.add(userRS.getString(i));
      }
      userRS.close();
      user.add(temp);

      ResultSet locationRS = DatabaseHandler.getInstance()
        .querySelect("select location_name from location\n" +
          "inner join staff_location l on location.location_id = l.fk_location_id\n" +
          "inner  join staff s on l.fk_staff_id = s.staff_id\n" +
          "where staff_id = " + Session.getId());


      while (locationRS.next())
        temp2.add(locationRS.getString(1));
      user.add(temp2);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }
}
