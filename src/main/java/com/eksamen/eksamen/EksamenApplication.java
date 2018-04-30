package com.eksamen.eksamen;

import com.eksamen.eksamen.Handler.EmailHandler;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EksamenApplication {

    public static void main(String[] args) {
        //SpringApplication.run(EksamenApplication.class, args);
      EmailHandler.getInstance().createMessage("developerteam1234@gmail.com", "testSub", "Singe" );
    }
}
