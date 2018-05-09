package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Base.Staff;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "/index";
    }

    @GetMapping("/medarbejderliste")
    public String employeeList(Model model){
        model.addAttribute("employee", new Staff());
        return "employeeList";
    }

}
