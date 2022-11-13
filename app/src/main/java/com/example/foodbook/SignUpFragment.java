package com.example.foodbook;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView tosignin;
    private FrameLayout parentframelayout;
    private EditText email;
    private EditText username;
    private EditText password;
    private EditText confirmpassword;

    private Button signupbtn;

    private FirebaseAuth firebaseAuth;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseFirestore firebaseFirestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        tosignin = view.findViewById(R.id.tosignin);
        email = view.findViewById(R.id.Sign_up_email);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.Sign_up_password);
        confirmpassword = view.findViewById(R.id.confirm_password);
        signupbtn = view.findViewById(R.id.sign_up);
        parentframelayout = getActivity().findViewById(R.id.register_framelayout);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tosignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

       email.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               checkinputs();
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
       username.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
       password.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
       confirmpassword.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentframelayout.getId(), fragment);
        fragmentTransaction.commit();
    }
    private void checkinputs(){
    if(!TextUtils.isEmpty(email.getText())){
        if(!TextUtils.isEmpty(username.getText())){
            if(!TextUtils.isEmpty(password.getText()) && password.length()>=5){
                if(!TextUtils.isEmpty(confirmpassword.getText())){
                    signupbtn.setEnabled(true);
                    signupbtn.setTextColor(Color.rgb(255, 255, 255));
                }else{
                    signupbtn.setEnabled(false);
                    signupbtn.setTextColor(Color.argb(50,255, 255,255));
                }
            }else{
                signupbtn.setEnabled(false);
                signupbtn.setTextColor(Color.argb(50,255, 255,255));
            }
        }else{
            signupbtn.setEnabled(false);
            signupbtn.setTextColor(Color.argb(50,255, 255,255));
        }

    }else{
        signupbtn.setEnabled(false);
        signupbtn.setTextColor(Color.argb(50,255, 255,255));
    }

    }
    private void checkEmailAndPassword(){
        if(email.getText().toString().matches(emailpattern)){
            if(password.getText().toString().equals(confirmpassword.getText().toString())){

                signupbtn.setEnabled(false);
                signupbtn.setTextColor(Color.argb(50,255, 255,255));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    Map<Object, String> userdata = new HashMap<>();
                                    userdata.put("User-id", username.getText().toString());

                                    firebaseFirestore.collection("USERS")
                                            .add(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if(task.isSuccessful()){
                                                        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                                        startActivity(mainIntent);
                                                        getActivity().finish();
                                                    }else{
                                                        signupbtn.setEnabled(true);
                                                        signupbtn.setTextColor(Color.rgb(255, 255, 255));
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }else{
                                    signupbtn.setEnabled(true);
                                    signupbtn.setTextColor(Color.rgb(255, 255, 255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }else{
                confirmpassword.setError("Password doesn't matched");
            }
        }else{
            email.setError("Invalid Email!");
        }
    }
}
