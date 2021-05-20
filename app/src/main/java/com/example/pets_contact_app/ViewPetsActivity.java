package com.example.pets_contact_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ViewPetsActivity extends AppCompatActivity {
    private TextView noPets;
    private ListView listView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pets);

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
                                List<Map<String, Object>> pets = (List<Map<String, Object>>) document.getData().get("pets");
                                if (pets.size() == 0){
                                    noPets = findViewById(R.id.noPets);
                                    noPets.setText("Looks like your dont have a pet on record. Please create a pet profile.");
                                }
                                else{
                                    MyAdapter adapter = new MyAdapter(ViewPetsActivity.this, pets);
                                    listView = findViewById(R.id.listView);
                                    listView.setAdapter(adapter);
                                }
                            }
                        }
                    }
                });
    }

    class MyAdapter extends ArrayAdapter<Map<String, Object>> {

        Context c;
        List<Map<String, Object>> pets;
        MyAdapter(Context c, List<Map<String, Object>> pets){
            super(c, R.layout.pet_display, pets);
            this.c = c;
            this.pets = pets;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.pet_display, container, false);
            }

            ((TextView) convertView.findViewById(R.id.petName))
                    .setText(pets.get(position).get("name").toString());
            ((TextView) convertView.findViewById(R.id.petAge))
                    .setText(pets.get(position).get("age").toString());
            ((TextView) convertView.findViewById(R.id.petWeight))
                    .setText(pets.get(position).get("weight").toString());
            ((TextView) convertView.findViewById(R.id.petSex))
                    .setText(pets.get(position).get("sex").toString());

            return convertView;
        }
    }



}