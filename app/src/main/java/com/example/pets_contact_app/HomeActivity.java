package com.example.pets_contact_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG =  "KLJ";
    private String UID;
    private TextView name;
    private Button makePet;
    private Button viewPets;
    private Button makeAppt;
    private Button viewAppts;

    private FirebaseFirestore db;
    private Map<String, Object> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name = (TextView) findViewById(R.id.name);
        makePet = (Button) findViewById(R.id.makePet);
        viewPets = (Button) findViewById(R.id.viewPets);
        makeAppt = (Button) findViewById(R.id.makeAppt);
        viewAppts = (Button) findViewById(R.id.viewAppts);


        db = FirebaseFirestore.getInstance();
        Bundle b = getIntent().getExtras();
        if(b != null)
            UID = b.getString("UID");

        db.collection("users").document(UID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                userData = document.getData();
//                                Log.i("DATA", userData.toString());
                                name.setText("Hello " + userData.get("firstName").toString());
                            }
                        }
                    }
                });

        makePet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CreatePetActivity.class));
            }
        });

        viewPets.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ViewPetsActivity.class));
            }
        });

        makeAppt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                db.collection("users").document(UID).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        List<Map<String, Object>> pets = (List<Map<String, Object>>) document.getData().get("pets");
                                        if (pets.size() == 0){
                                            Toast.makeText(HomeActivity.this, "Please make a profile for your pet before making an appointment.", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            startActivity(new Intent(HomeActivity.this, CreateAppointmentActivity.class));

                                        }
                                    }
                                }
                            }
                        });
            }
        });

        viewAppts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AppointmentHistoryActivity.class));
            }
        });

    }
    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent newIntent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(newIntent);
        finish();
    }
}