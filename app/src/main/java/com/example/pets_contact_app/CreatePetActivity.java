package com.example.pets_contact_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class CreatePetActivity extends AppCompatActivity {

    private EditText petName;
    private EditText petBreed;
    private EditText petWeight;
    private EditText petAge;
    private Spinner petSex;
    private Spinner age;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pet);

        mAuth = FirebaseAuth.getInstance();
        petName = (EditText) findViewById(R.id.editTextPetName);
        petBreed = (EditText) findViewById(R.id.editTextBreed);
        petWeight = (EditText) findViewById(R.id.editTextPetWeight);
        petAge = (EditText) findViewById(R.id.editTextPetAge);
        petSex = (Spinner) findViewById(R.id.spinner);
        age = (Spinner) findViewById(R.id.spinner2);

    }

    public void validatePet(View v){
        int selectedAgePos = (int) age.getSelectedItemId();
        int selectedSexPos = (int) petSex.getSelectedItemId();
        String name = petName.getText().toString();
        String breed = petBreed.getText().toString();
        String weight = petWeight.getText().toString();
        String ageStr = petAge.getText().toString();
        if (selectedAgePos > 0 && selectedSexPos > 0 && name.length() > 0 && breed.length() >0 && weight.length() > 0 && ageStr.length() > 0) {
                Pet pet = new Pet(
                        name,
                        breed,
                        petSex.getSelectedItem().toString(),
                        Double.valueOf(weight),
                        ageStr + " " + age.getSelectedItem().toString()
                        );
                updatePetDB(pet);
//                Log.i("Pet", pet.toMap().toString());
            } else {
                Toast.makeText(CreatePetActivity.this, "You left a field empty. Please Initialize All Fields.", Toast.LENGTH_LONG).show();

            }


    }

    private void updatePetDB(Pet pet) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String UID = mAuth.getCurrentUser().getUid();
        db.collection("users")
                .document(UID)
                .update("pets", FieldValue.arrayUnion(pet.toMap()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void _) {
                        Toast.makeText(CreatePetActivity.this, "Your pet has successfully been created!", Toast.LENGTH_SHORT).show();
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