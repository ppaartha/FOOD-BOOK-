package com.example.foodbook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.foodbook.Adapter.PostAdapter;
import com.example.foodbook.Model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Homefragement extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postLists;

    private List<String> followingList;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_homefragement, container, false);

        recyclerView=view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists=new ArrayList<Post>();
        postAdapter=new PostAdapter(getContext(),postLists);
        recyclerView.setAdapter(postAdapter);

        progressBar=view.findViewById(R.id.progress_circular);

        checkFollowing();
        return view;
    }


    private  void checkFollowing(){
        followingList =new ArrayList<String>();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void readPosts(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Post Recipe");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postLists.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String pid = snapshot.child("postid").getValue().toString();
                    String img = snapshot.child("recipeimage").getValue().toString();
                    String name = snapshot.child("recipename").getValue().toString();
                    String des = snapshot.child("description").getValue().toString();
                    String ingredients = snapshot.child("ingredients").getValue().toString();
                    String publisher = snapshot.child("publisher").getValue().toString();
                    //Post post= snapshot.getValue(Post.class);
                    Post post = new Post(pid,img,name,des,ingredients,publisher);
                    postLists.add(post);

                 for(String id: followingList){
                        if(post.getPublisher().equals(id)){
                            postLists.add(post);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
