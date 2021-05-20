package com.example.pets_contact_app;

import com.google.firebase.firestore.PropertyName;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/*
    This is the Appointment model class
*/
public class Appointment {
    public LocalDate date;
    public String name;
    public String pet;

    Appointment(String name, LocalDate date, String pet){
        this.date = date;
        this.name = name;
        this.pet = pet;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> apptJson = new HashMap<>();

        apptJson.put("name", this.name);
        apptJson.put("date", this.date.toString());
        apptJson.put("pet", this.pet);

        return apptJson;
    }

    public Appointment fromMap(Map<String, Object> map){
        return new Appointment(
                map.get("name").toString(),
                (LocalDate) map.get("date"),
                map.get("sex").toString()
        );
    }
}
