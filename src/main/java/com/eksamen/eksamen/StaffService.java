package com.eksamen.eksamen;

import com.eksamen.eksamen.Base.Location;
import com.eksamen.eksamen.Base.Session;
import com.eksamen.eksamen.Base.Staff;
import com.eksamen.eksamen.Handler.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StaffService {
  public static int getIdForStaff = 0;

  /*
  Method to get all the locations in the database into an array
  */
  public static ArrayList<Location> getLocations() {
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

  public static int getEmployeeId(String email) {

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

  public static Staff getStaff(String email) {
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
   * Removes employee from database
   */
  public static void deleteForEmployee(String table, String condition){
    DatabaseHandler.getInstance().delete(
        "DELETE FROM "+
            table+" "+
            "WHERE "+condition+"= "+getIdForStaff+"; "
    );
  }

  public static ArrayList getCurrentLocations(){
    ArrayList currentLocations = new ArrayList();
    System.out.println(getIdForStaff);

    ResultSet resultSet = DatabaseHandler.getInstance().querySelect(
        "SELECT fk_location_id "+
        "FROM staff_location "+
        "WHERE fk_staff_id = "+getIdForStaff
    );

    try {
      while (resultSet.next()) {
        currentLocations.add(resultSet.getInt("fk_location_id"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    System.out.println(currentLocations);

    return currentLocations;
  }
}
