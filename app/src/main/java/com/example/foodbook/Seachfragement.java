package com.example.foodbook;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodbook.Adapter.PostAdapter;
import com.example.foodbook.Adapter.UserAdapter;
import com.example.foodbook.Model.Post;
import com.example.foodbook.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Seachfragement extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private List<Post>mPosts;
    EditText search_bar;
    private PostAdapter postAdapter;
    SwitchCompat switchCompat;
    String checkValue = "receipe";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_seachfragement, container, false);
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        search_bar= view.findViewById(R.id.search_bar);
        switchCompat=view.findViewById(R.id.switchbutton);
        mUsers =new ArrayList<>();
        mPosts=new ArrayList<>();
        userAdapter =new UserAdapter(getContext(),mUsers,true);
        postAdapter=new PostAdapter(getContext(),mPosts);
        //recyclerView.setAdapter(postAdapter);
        readUsers();
        readRecipes();
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchCompat.isChecked())
                {
                    checkValue = "user";

                }
                else checkValue = "receipe";

            }
        });

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 Log.d("fffffff", charSequence.toString());
                 if(checkValue == "receipe"){
                     searchUsers(charSequence.toString().toLowerCase());
                     recyclerView.setAdapter(userAdapter);
                 }
                 else{
                     searchRecipe(charSequence.toString());
                     recyclerView.setAdapter(postAdapter);
                 }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    private void searchUsers(String s){
        Query query= FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    mUsers.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private  void readUsers(){
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(search_bar.getText().toString().equals("")){
                    mUsers.clear();
                    for(DataSnapshot snapshot :dataSnapshot.getChildren())
                    {
                        User user= snapshot.getValue(User.class);
                        mUsers.add(user);
                    }
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchRecipe(String s) {
        Log.d("IN Func", s);
        Query query = FirebaseDatabase.getInstance().getReference("Post Recipe").orderByChild("recipename")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPosts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    mPosts.add(post);
                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private  void readRecipes(){
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Post Recipe");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(search_bar.getText().toString().equals("")){
                    mPosts.clear();
                    for(DataSnapshot snapshot :dataSnapshot.getChildren())
                    {
                        Post post= snapshot.getValue(Post.class);
                        mPosts.add(post);
                    }
                    postAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
