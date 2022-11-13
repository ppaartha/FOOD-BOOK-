package com.example.foodbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username,fullname,email,password;
    Button register;
    TextView txt_signin;
    FirebaseAuth auth;
    DatabaseReference reference,reference1;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname=findViewById(R.id.fullname);
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        txt_signin=findViewById(R.id.txt_signin);

        auth=FirebaseAuth.getInstance();
        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd=new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please Wait........");
                pd.show();
                String str_username=username.getText().toString();
                String str_fullname=fullname.getText().toString();
                String str_email=email.getText().toString();
                String str_password=password.getText().toString();

                if(TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname)|| TextUtils.isEmpty(str_email)||TextUtils.isEmpty(str_password)){
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else if(str_password.length()<6){
                    Toast.makeText(RegisterActivity.this, "Password must have 6 characters", Toast.LENGTH_SHORT).show();
                } else{
                    register(str_username,str_fullname,str_email,str_password);

                }

            }
        });

    }
    private void register(final String username, final String fullname, String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userid=firebaseUser.getUid();
                            reference= FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(userid);
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username.toLowerCase());
                            hashMap.put("fullname",fullname);
                            hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/foodbook-5f6bf.appspot.com/o/searchpng.com-deafult-profile-icon-transparent-png-free-download.png?alt=media&token=8097013f-00b9-4699-a468-e79c79f628ed");

                            reference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("Follow");
                            HashMap<String,Object> hashMap1=new HashMap<>();
                            hashMap1.put("followers",0);
                            hashMap1.put("following",0);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        pd=new ProgressDialog(RegisterActivity.this);
                                        pd.setMessage("Account is Successfully created!!");
                                        pd.show();
                                        pd.dismiss();
                                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });

                            reference1.setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        pd=new ProgressDialog(RegisterActivity.this);
                                        pd.setMessage("Account is Successfully created!!");
                                        pd.show();
                                        pd.dismiss();
                                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });


                        }else{
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

}
