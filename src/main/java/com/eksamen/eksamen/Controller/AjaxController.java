package com.eksamen.eksamen.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AjaxController {

  @PostMapping("/hello")
  public String getUser(){
    return null;
  }
}
