package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Handler.DatabaseHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.sql.ResultSet;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "/index";
    }

    @GetMapping("/medarbejderliste")
    public String employeeList(){
        ResultSet rs = DatabaseHandler.getInstance()
          .selectAll("staff");
        try {
            while (rs.next())
            System.out.println(rs.getString("firstname"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "employeeList";
    }
}
