package com.example.pets_contact_app;

import java.util.HashMap;
import java.util.Map;

/*
    This is the Pet model class
*/

public class Pet {
    String name;
    String breed;
    String sex;
    double weight;
    String age;

    Pet(String name, String breed, String sex, double weight, String age){
        this.name = name;
        this.breed = breed;
        this.sex = sex;
        this.weight = weight;
        this.age = age;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> petJson = new HashMap<>();

        petJson.put("name", this.name);
        petJson.put("breed", this.breed);
        petJson.put("sex", this.sex);
        petJson.put("weight", this.weight);
        petJson.put("age", this.age);

        return petJson;
    }

    public Pet fromMap(Map<String, Object> map){
        return new Pet(
                map.get("name").toString(),
                map.get("breed").toString(),
                map.get("sex").toString(),
                Double.valueOf(map.get("weight").toString()),
                map.get("age").toString()
        );
    }
}
