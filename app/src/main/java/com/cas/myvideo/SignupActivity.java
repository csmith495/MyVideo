package com.cas.myvideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    EditText nameBox, emailBox, passwordBox;
    Button createBtn, returnBtn;
    FirebaseAuth auth;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameBox = findViewById(R.id.nameBox);
        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        createBtn = findViewById(R.id.createBtn);
        returnBtn = findViewById(R.id.returnBtn);

        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail, sPassword, sName;
                sEmail = emailBox.getText().toString();
                sPassword = passwordBox.getText().toString();
                sName = nameBox.getText().toString();

                User user = new User();
                user.setEmail(sEmail);
                user.setPassword(sPassword);
                user.setName(sName);

                auth.createUserWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            database.collection("Users")
                                .document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                }
                            });
                            Toast.makeText(SignupActivity.this,
                                    "Account Created!!", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(SignupActivity.this,
                                    "Error: Account Creation Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}