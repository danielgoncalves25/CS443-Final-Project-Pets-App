package com.example.pets_contact_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private static final String TAGS =  "KLJ";
    //    private String TAG = "Doc";
    private String UID;
    private TextView name;
    private FirebaseFirestore db;
    private Map<String, Object> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        name = (TextView) findViewById(R.id.name);
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
                                Log.i("DATA", userData.toString());
                                name.setText("Hello " + userData.get("firstName").toString());
                            }
                        }
                    }
                });

//        Toast.makeText(HomeActivity.this, UID, Toast.LENGTH_LONG).show();
//        Pet keesh = new Pet("Keesh", "Pitbull Terrier", "Female", 56.12, 1);
//        Map<String, Object> ex = keesh.toMap();
//        Log.d("Pet", keesh.name);
    }
    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent newIntent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(newIntent);
        finish();
    }
}