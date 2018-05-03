package com.eksamen.eksamen;

import com.eksamen.eksamen.Handler.DatabaseHandler;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class EksamenApplication {

    public static void main(String[] args) {
        //SpringApplication.run(EksamenApplication.class, args);
        //DatabaseHandler.getInstance().insert("asd", new String{"asd"}, new ArrayList("asdasd", "asd"));
        //EmailHandler.getInstance().createMessage("marco@romeri.dk", "testSub", "<h1>Bøgse</h1><hr><p>Singe</p>" );
        ArrayList<String> columns = DatabaseHandler.getInstance().getColumns("shift");
        for (String s: columns) {
            System.out.println(s);
        }
        //System.out.println(Operation.INSERT);
    }
}
