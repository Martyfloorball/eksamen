package com.eksamen.eksamen.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionController {
  @GetMapping("/login")
  public String login() {
    return "login";
  }
}
