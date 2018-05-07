package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Base.Session;
import com.eksamen.eksamen.Handler.DatabaseHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.ResultSet;
import java.sql.SQLException;


@Controller
public class SessionController {
    String failure = "";

    @GetMapping("/login")
    public String login(Model model) {
      if(!Session.isLoggedIn()){
        model.addAttribute("login-failure", failure);
        return "login";
      }else {
        return "redirect:/";
      }
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam("email") String email, @RequestParam("password") String password) {
      password = new BCryptPasswordEncoder().encode(password);

      try {
        ResultSet resultSet = DatabaseHandler.getInstance().select("staff",
            "COUNT(staff_id) AS total, staff_id, fk_staff_niveau_id",
            "WHERE email = '"+email+"' AND password = '"+password+"'",
            "",
            0,
            "",
            "");
        if(resultSet.getInt(1) == 1){
          Session.setId(resultSet.getInt(2));
          Session.setUserniveau(resultSet.getInt(3));
          failure = "";
          return "redirect:/";
        }else{
          failure = "Ugyldigt login";
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return "redirect:/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
      if(!Session.isLoggedIn()){
        model.addAttribute("forgot-failure", failure);
        return "forgot-password";
      }
      return "redirect:/";
    }

    @PostMapping("/forgot-password")
    public String forgotSubmit(@RequestParam("email") String email) {
      return "redirect:/forgot-password";
    }
}
