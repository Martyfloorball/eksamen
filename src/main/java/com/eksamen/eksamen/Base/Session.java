package com.eksamen.eksamen.Base;

public class Session {
    private static int id = 4;
    private static int userniveau;

    //Hvis id ikke er 0 returneres true, og brugeren bliver logget ind
    public static boolean isLoggedIn(){
        return id != 0;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Session.id = id;
    }

    public static int getUserniveau() {
        return userniveau;
    }

    public static void setUserniveau(int userniveau) {
        Session.userniveau = userniveau;
    }
}
