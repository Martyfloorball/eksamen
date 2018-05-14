package com.eksamen.eksamen.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShiftController {

  @GetMapping("/vagtplan")
  public String vagtplan(){
    return "schedule";
  }
}
