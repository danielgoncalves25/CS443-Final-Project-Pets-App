package com.example.pets_contact_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateAppointmentActivity extends AppCompatActivity {

    private EditText name;
    private CalendarView date;
    private Spinner petSpinner;
    private LocalDate apptDate;
    private Button submit;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        name = findViewById(R.id.editTextPersonName);
        date = findViewById(R.id.calendarView);
        petSpinner = findViewById(R.id.spinner3);
        submit = findViewById(R.id.buttonCreateAppt);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();
        db.collection("users").document(UID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> userData = document.getData();
                                name.setText(userData.get("firstName").toString() + " "+ userData.get("lastName").toString());
                                List<Map<String, Object>> petsList = (List<Map<String, Object>>) userData.get("pets");
                                List<String> petsName = new ArrayList<String>();
                                petsName.add("Select a pet");
                                for (int i = 0; i < petsList.size(); i++){
                                    petsName.add(petsList.get(i).get("name").toString());
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateAppointmentActivity.this, R.layout.support_simple_spinner_dropdown_item, petsName);
                                petSpinner.setAdapter(adapter);
                            }
                        }
                    }
                });

        date.setMinDate(date.getDate());
        date.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                apptDate = LocalDate.of(year,month, dayOfMonth);
//                Log.i("Date", apptDate.toString());
            }
        });

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String personName = name.getText().toString();
                int pos  = petSpinner.getSelectedItemPosition();

                if (personName.length() > 0 && pos != 0) {
                    Appointment appt = new Appointment(personName, apptDate, petSpinner.getSelectedItem().toString());
                    Log.i("appt", appt.toMap().toString());
                    updateAppointmentDB(appt);
                } else {
                    Toast.makeText(CreateAppointmentActivity.this, "You left a field empty. Please Initialize All Fields.", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void updateAppointmentDB(Appointment apt){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String UID = mAuth.getCurrentUser().getUid();
        db.collection("users")
                .document(UID)
                .update("appointments", FieldValue.arrayUnion(apt.toMap()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void _) {
                        Toast.makeText(CreateAppointmentActivity.this, "Your pet has successfully been created!", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
    }
}