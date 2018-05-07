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
        return "employeeList";
    }

}
