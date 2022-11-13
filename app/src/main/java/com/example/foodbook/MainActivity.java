package com.example.foodbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment =null;
    public static String uid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.main_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemselectedListener);
        Bundle intent =getIntent().getExtras();
        if(intent!=null){
            String publisher =intent.getString("publisheid");

            SharedPreferences.Editor editor= getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("profileid",publisher);
            editor.apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new Profilefragement()).commit();

        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new Homefragement()).commit();

        }

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new Homefragement()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemselectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selectedFragment =new Homefragement();

                break;
                case R.id.nav_search:
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    selectedFragment =new Seachfragement();
                    break;

                case R.id.nav_upload:
                    selectedFragment =null;
                    startActivity(new Intent(MainActivity.this,PostActivity.class));
                    break;

                case R.id.nav_notification:
                    selectedFragment =new Notificationfragement();
                    break;

                case R.id.nav_profile:
                    SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                    editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();
                    selectedFragment =new Profilefragement();
                    break;

            }
            if(selectedFragment !=null)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, selectedFragment).commit();
            }
            return true;
        }
    };
}
