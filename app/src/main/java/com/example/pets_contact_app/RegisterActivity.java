package com.example.pets_contact_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "Signing Up";
    private FirebaseAuth mAuth;
    private Button registerButton;
    private EditText password;
    private EditText emailAddress;
    private EditText firstName;
    private EditText lastName;
    private TextView goToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = (Button) findViewById(R.id.buttonRegister);
        emailAddress = (EditText) findViewById(R.id.editTextEmailRegister);
        password = (EditText) findViewById(R.id.editTextPasswordRegister);
        firstName = (EditText) findViewById(R.id.editTextFirstName);
        lastName = (EditText) findViewById(R.id.editTextLastName);

        goToLogin = findViewById(R.id.textViewRegister);
        mAuth = FirebaseAuth.getInstance();

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }

    public void signUp(View v) {
        String email = emailAddress.getText().toString();
        String pass = password.getText().toString();
        String first = firstName.getText().toString();
        String last = lastName.getText().toString();

        if (email.length() > 0 && pass.length() > 0 && first.length() > 0 & last.length() > 0)
            mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            initUserDB(user.getUid());
                            Intent newIntent = new Intent(RegisterActivity.this, HomeActivity.class);
                            Bundle b = new Bundle();
                            b.putString("UID", user.getUid());
                            newIntent.putExtras(b);
                            startActivity(newIntent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        else Toast.makeText(this, "You left a field empty. Please Initialize All Fields", Toast.LENGTH_LONG).show();
    }

    public void initUserDB(String UID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userData= new HashMap<>();
        List<Appointment> appointments = new ArrayList<>();
        List<Pet> pets = new ArrayList();

        userData.put("firstName", firstName.getText().toString());
        userData.put("lastName", lastName.getText().toString());
        userData.put("appointments", appointments);
        userData.put("pets", pets);

        db.collection("users").document(UID).set(userData);
    }
}