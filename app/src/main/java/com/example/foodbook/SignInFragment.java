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


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    public SignInFragment() {
        // Required empty public constructor
    }

    private TextView tosignup;
    private FrameLayout parentframelayout;
    private EditText email;
    private EditText password;

    private Button signinbtn;
    private FirebaseAuth firebaseAuth;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        tosignup = view.findViewById(R.id.tosignup);
        email = view.findViewById(R.id.Sign_in_Email);
        password = view.findViewById(R.id.Sign_in_Password);
        signinbtn = view.findViewById(R.id.sign_in);
        firebaseAuth = FirebaseAuth.getInstance();
        parentframelayout = getActivity().findViewById(R.id.register_framelayout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
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
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentframelayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs(){
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(password.getText())){
                signinbtn.setEnabled(true);
                signinbtn.setTextColor(Color.rgb(255, 255, 255));
            }else{
                signinbtn.setEnabled(false);
                signinbtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        }else{
            signinbtn.setEnabled(false);
            signinbtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }
    private void checkEmailAndPassword(){
        if(email.getText().toString().matches(emailpattern)){
            if(password.length()>=5){

                signinbtn.setEnabled(false);
                signinbtn.setTextColor(Color.argb(50, 255, 255, 255));
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(mainIntent);
                                    getActivity().finish();
                                }else{
                                    signinbtn.setEnabled(true);
                                    signinbtn.setTextColor(Color.rgb(255, 255, 255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else{
                Toast.makeText(getActivity(), "Incorrect email or password!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Incorrect email or password!", Toast.LENGTH_SHORT).show();
        }
    }
}
