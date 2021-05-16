package com.example.pets_contact_app;

import java.util.Date;
/*
    This is the Appointment model class
*/
public class Appointment {
    Date date;
    String name;
    String reason;
    Pet pet;

    Appointment(Date date, String name, String reason, Pet pet){
        this.date = date;
        this.name = name;
        this.reason = reason;
        this.pet = pet;
    }


}
