package com.example.pets_contact_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppointmentHistoryActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView apptList;
    private TextView noAppts;
    private Spinner petSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth myAuth = FirebaseAuth.getInstance();
        String UID = myAuth.getCurrentUser().getUid();

        petSpinner = findViewById(R.id.spinner4);
        noAppts = findViewById(R.id.noAppts);

        db.collection("users").document(UID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> userData = document.getData();
                                List<Map<String, Object>> petsList = (List<Map<String, Object>>) userData.get("pets");

//                              populate Spinner with pet names
                                List<String> petsName = new ArrayList<String>();
                                petsName.add("All");
                                for (int i = 0; i < petsList.size(); i++){
                                    petsName.add(petsList.get(i).get("name").toString());
                                }
                                ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(AppointmentHistoryActivity.this, R.layout.support_simple_spinner_dropdown_item, petsName);
                                petSpinner.setAdapter(adapterSpinner);

//                              populate the ListView with appointments
                                List<Map<String, Object>> apptsJson = (List<Map<String, Object>>) userData.get("appointments");
                                noAppts.setText(apptsJson.size() > 0 ? "" : "Looks like you don't have any appointments");
                                AppointmentAdapter adapterAppt = new AppointmentAdapter(AppointmentHistoryActivity.this, apptsJson);
                                apptList = findViewById(R.id.apptList);
                                apptList.setAdapter(adapterAppt);
                            }
                        }
                    }
                });

        petSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                db.collection("users").document(UID).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> userData = document.getData();
                                        List<Map<String, Object>> apptsJson = (List<Map<String, Object>>) userData.get("appointments");
                                        List<Map<String, Object>> filteredAppts = new ArrayList<Map<String, Object>>();
                                        if (i > 0){
                                            String selectedPet = petSpinner.getSelectedItem().toString();
                                            for(int index = 0; index < apptsJson.size(); index++){
                                                if (apptsJson.get(index).get("pet").equals(selectedPet)){
                                                    filteredAppts.add(apptsJson.get(index));
                                                }
                                            }
                                            noAppts.setText(filteredAppts.size() == 0 ? "Looks like you don't have an appointment with " + selectedPet : "");
                                        } else noAppts.setText("");

//                                      filter appointment view with selected pet
                                        AppointmentAdapter adapterAppt = new AppointmentAdapter(AppointmentHistoryActivity.this, (i > 0) ? filteredAppts : apptsJson);
                                        apptList = findViewById(R.id.apptList);
                                        apptList.setAdapter(adapterAppt);

                                    }
                                }
                            }
                        });
                }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }


    class AppointmentAdapter extends ArrayAdapter<Map<String, Object>> {

        Context c;
        List<Map<String, Object>> appts;
        AppointmentAdapter(Context c, List<Map<String, Object>> appts){
            super(c, R.layout.appointment_display, appts);
            this.c = c;
            this.appts = appts;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.appointment_display, container, false);
            }

            ((TextView) convertView.findViewById(R.id.apptName))
                    .setText(appts.get(position).get("name").toString());
            ((TextView) convertView.findViewById(R.id.apptDate))
                    .setText(appts.get(position).get("date").toString());
            ((TextView) convertView.findViewById(R.id.apptPet))
                    .setText(appts.get(position).get("pet").toString());

            return convertView;
        }
    }

}