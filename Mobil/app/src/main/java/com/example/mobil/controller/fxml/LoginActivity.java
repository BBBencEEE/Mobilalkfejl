package com.example.mobil.controller.fxml;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.mobil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    public static final String LOG_TAG = LoginActivity.class.getName();

    private FirebaseAuth mAuth;

    EditText emailET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void cancel(View view) {
        finish();
    }

    public void login(View view) {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG,"Logged in");
                    startIntent();
                }else{
                    Log.d(LOG_TAG,"Cannot log in");
                }
            }
        });
    }

    private void startIntent(){
        Intent intent = new Intent(this, ShopListActivity.class);
        startActivity(intent);
    }
}