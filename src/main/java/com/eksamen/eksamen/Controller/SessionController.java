package com.eksamen.eksamen.Controller;

import com.eksamen.eksamen.Base.Session;
import com.eksamen.eksamen.Handler.DatabaseHandler;
import com.eksamen.eksamen.Handler.EmailHandler;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    String message = "";

    @GetMapping("/login")
    public String login(Model model) {
      if(!Session.isLoggedIn()){
        model.addAttribute("login-failure", failure);
        return "login";
      }else {
        return "redirect:/";
      }
    }

    @PostMapping(value = "/login", params = "login-submit")
    public String loginSubmit(@RequestParam("email") String email, @RequestParam("password") String password) {
      try {
        ResultSet resultSet = DatabaseHandler.getInstance().select("staff",
            "staff_id, fk_staff_niveau_id, password",
            "WHERE email = '"+email+"'",
            "",
            0,
            "GROUP BY staff_id",
            "");

        if(resultSet.next() && new BCryptPasswordEncoder().matches(email+password, resultSet.getString("password"))){
          Session.setId(resultSet.getInt("staff_id"));
          Session.setUserniveau(resultSet.getInt("fk_staff_niveau_id"));
          failure = "";
          return "redirect:/";
        } else{
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

    @PostMapping(value = "/forgot-password", params = "forgot-submit")
    public String forgotSubmit(@RequestParam("email") String email) {
      try {
        ResultSet resultSet = DatabaseHandler.getInstance().select("staff",
            "staff_id, fk_staff_niveau_id, password",
            "WHERE email = '"+email+"'",
            "",
            0,
            "GROUP BY staff_id",
            "");

        if(resultSet.next()){
          String characters = "ABCDEFGHJKMNOPQRSTUVWXYZabcdefghjkmnopqrstuvwxyz023456789";
          String newPassword = RandomStringUtils.random( 10, characters );
          EmailHandler.getInstance().createMessage("developerteam1234@gmail.com","New Password", "Her er dit nye password: "+newPassword);

          failure = "";
          message = "Email sendes til din email.";
          return "redirect:/login";
        } else{
          failure = "Email findes ikke.";
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }


      return "redirect:/forgot-password";
    }
}
