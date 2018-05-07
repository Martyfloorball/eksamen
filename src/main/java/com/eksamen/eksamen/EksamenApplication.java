package com.eksamen.eksamen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class EksamenApplication {

    public static void main(String[] args) {
        SpringApplication.run(EksamenApplication.class, args);
        //DatabaseHandler.getInstance().insert("asd", new String{"asd"}, new ArrayList("asdasd", "asd"));
        //EmailHandler.getInstance().createMessage("marco@romeri.dk", "testSub", "<h1>Bøgse</h1><hr><p>Singe</p>" );

    }
}
