package com.example.foodbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        firebaseAuth = FirebaseAuth.getInstance();
        SystemClock.sleep(2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent1 = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
        }
    }
}
