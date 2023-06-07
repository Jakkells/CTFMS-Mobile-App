package com.example.ctfmsproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button loginButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(flags);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://ctfms-project-default-rtdb.europe-west1.firebasedatabase.app/");
        usersRef = database.getReference("Users");

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        // Get user input from EditText fields
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validate user input
        if (email.isEmpty() || password.isEmpty()) {
            // Display an error message to the user
            Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Authenticate the user with Firebase Authentication
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User authentication successful
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();

                                // Retrieve user information from Realtime Database
                                usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            // User exists in the database
                                            User user = snapshot.getValue(User.class);
                                            if (user != null) {
                                                // Check user role (manager or not)
                                                boolean isManager = user.isManager();
                                                // Navigate to the appropriate activity based on user role
                                                if (isManager) {
                                                    navigateToManagerActivity();
                                                } else {
                                                    navigateToEmployeeActivity();
                                                }
                                            } else {
                                                // User object is null
                                                Toast.makeText(LoginActivity.this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            // User does not exist in the database
                                            Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Error occurred while retrieving user information
                                        Toast.makeText(LoginActivity.this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            // User authentication failed
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void navigateToManagerActivity() {
        Intent intent = new Intent(LoginActivity.this, ManagerView1.class);
        startActivity(intent);
        finish();
    }

    private void navigateToEmployeeActivity() {
        Intent intent = new Intent(LoginActivity.this, EmployeeView1.class);
        startActivity(intent);
        finish();
    }
}
