package com.example.mobil.controller.fxml;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    public static final String LOG_TAG = SignUpActivity.class.getName();
    private FirebaseAuth mAuth;

    EditText usernameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameET = findViewById(R.id.username);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        passwordAgainET = findViewById(R.id.passwordAgain);

        mAuth = FirebaseAuth.getInstance();
    }


    public void cancel(View view) {
        finish();
    }

    public void signUp(View view) {
        String username = usernameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();

        if(!password.equals(passwordAgain)){
            Log.e(LOG_TAG,"Passwords does not mach");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.e(LOG_TAG,"Signed up");
                    startIntent();
                }else{
                    Log.e(LOG_TAG,"Sign up error");
                    Toast.makeText(SignUpActivity.this,"fail: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void startIntent(){
        Intent intent = new Intent(this, ShopListActivity.class);
        startActivity(intent);
    }
}