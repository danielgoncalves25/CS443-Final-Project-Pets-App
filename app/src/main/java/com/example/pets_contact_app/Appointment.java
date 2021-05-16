package com.example.pets_contact_app;

import java.util.Date;
/*
    This is the Appointment model class
*/
public class Appointment {
    Date date;
    String name;
    String reason;
    Pet petName;

    Appointment(Date date, String name, String reason, Pet petName){
        this.date = date;
        this.name = name;
        this.reason = reason;
        this.petName = petName;
    }


}
