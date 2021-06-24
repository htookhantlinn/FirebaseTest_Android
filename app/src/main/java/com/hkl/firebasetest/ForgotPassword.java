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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button resetPasswordBtn;
    EditText editTextEmail;
    FirebaseAuth auth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        resetPasswordBtn=(Button) findViewById(R.id.resetBtn);
        editTextEmail=(EditText) findViewById(R.id.editTextResetEmail);
        progressBar=(ProgressBar) findViewById(R.id.progressBar3);

        auth=FirebaseAuth.getInstance();

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String email=editTextEmail.getText().toString().trim();
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            editTextEmail.setError("Please Provide Valid Email");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Check email",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                }else{
                    Toast.makeText(ForgotPassword.this," Something Wrong",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                }
            }
        });

    }
}