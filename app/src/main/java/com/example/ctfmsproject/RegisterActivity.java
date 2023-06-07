package com.example.ctfmsproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ctfmsproject.LoginActivity;
import com.example.ctfmsproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextMobileNo;
    private EditText editTextIdNo;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private RadioButton radioButtonManager;
    private RadioButton radioButtonEmployee;
    private Button registerButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerpage);
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(flags);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://ctfms-project-default-rtdb.europe-west1.firebasedatabase.app/");
        usersRef = database.getReference("Users");



        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextMobileNo = findViewById(R.id.editTextMobileNo);
        editTextIdNo = findViewById(R.id.editTextIdNo);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        radioButtonManager = findViewById(R.id.radioButtonManager);
        radioButtonEmployee = findViewById(R.id.radioButtonEmployee);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        // Get user input from EditText fields
        String name = editTextName.getText().toString().trim();
        String surname = editTextSurname.getText().toString().trim();
        String mobileNo = editTextMobileNo.getText().toString().trim();
        String idNo = editTextIdNo.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        boolean isManager = radioButtonManager.isChecked();

        // Validate user input
        if (name.isEmpty() || surname.isEmpty() || mobileNo.isEmpty() || idNo.isEmpty() ||
                email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // Display an error message to the user
            Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            // Display an error message to the user
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user in Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registration successful
                            String userId = firebaseAuth.getCurrentUser().getUid();

                            // Save the additional user details to Realtime Database
                            User user = new User(name, surname, mobileNo, idNo, email, isManager);
                            usersRef.child(userId).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // User details saved successfully
                                                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                // Navigate to the login activity
                                                navigateToLoginActivity();
                                            } else {
                                                // Error occurred while saving user details to Realtime Database
                                                Toast.makeText(RegisterActivity.this, "Failed to save user details", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // User registration failed
                            Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void navigateToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
