package com.hkl.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private EditText edtFullname, edtAge, edtEmail, edtPassword;
    private Button register;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        edtFullname = (EditText) findViewById(R.id.editTextFullName);
        edtAge = (EditText) findViewById(R.id.editTextAge);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtPassword = (EditText) findViewById(R.id.editTextPassword);

        register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(this);

        progressBar=(ProgressBar) findViewById(R.id.progressBar2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String age = edtAge.getText().toString().trim();
        String fullName = edtFullname.getText().toString().trim();

        if (fullName.isEmpty() || fullName.equals("")) {
            edtFullname.setError("Full Name is required");
            edtFullname.requestFocus();
            return;
        }

        if (age.isEmpty()) {
            edtAge.setError("Age is required");
            edtAge.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
            return;
        }

        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            edtEmail.setError("Please Provide Valid Email");
            edtEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            edtPassword.setError("Password  is required");
            edtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edtPassword.setError("Minimum password length should be 6 characters.");
            edtPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                System.out.println("IN oncomplete");
                if (task.isSuccessful()) {
                    User user = new User(fullName, age, email);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Register.this,MainActivity.class));
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Register.this, "Registration Failed !! Try Again", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}