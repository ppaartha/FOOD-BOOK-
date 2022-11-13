package com.example.foodbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodbook.Model.Post;
import com.example.foodbook.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class PostDetailActivity extends AppCompatActivity {
    String uid;
    StorageReference storageReference;
    ImageView image_profile,recipeimage,publisher_profile;
    TextView publisher,description,ingredients,time,recipe_name,username;
    String publiserID="";

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        username = findViewById(R.id.username);
        recipe_name = findViewById(R.id.recipe_name);
        description = findViewById(R.id.description);
        ingredients = findViewById(R.id.ingredients);
        publisher = findViewById(R.id.username);
        recipeimage = findViewById(R.id.recipe_image);
        publisher_profile = findViewById(R.id.publisher_profile);
        image_profile = findViewById(R.id.image_profile);
        time = findViewById(R.id.time);
        Intent intent = getIntent();
        uid = intent.getStringExtra("Post ID");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Post Recipe").child(uid);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Post Recipe").child(uid);


        //publisherInfo(image_profile, username, publisher, reference1);
        // getComments(uid,comments);


/*        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("publisherid",post.getPublisher());
                mContext.startActivity(intent);

            }
        });*/
/*        viewHolder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("publisherid",post.getPublisher());
                mContext.startActivity(intent);

            }
        });*/
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                Glide.with(getApplicationContext()).load(post.getRecipeimage()).into(recipeimage);
                description.setText(post.getDescription());
                ingredients.setText(post.getIngredients());
                publiserID = post.getPublisher();
                setImageAndName(publiserID);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void setImageAndName(String user){
        final DatabaseReference referencePublisher = FirebaseDatabase.getInstance().getReference("Users").child(user);
        referencePublisher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
               Glide.with(getApplicationContext()).load(user.getImageurl()).into(publisher_profile);
               username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
