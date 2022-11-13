package com.example.foodbook.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodbook.Model.Notification;

import com.example.foodbook.Model.Post;
import com.example.foodbook.Model.User;
import com.example.foodbook.Profilefragement;
import com.example.foodbook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private Context mContext;
    private List<Notification> mNotification;

    public NotificationAdapter(Context mContext, List<Notification> mNotification) {
        this.mContext = mContext;
        this.mNotification = mNotification;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.notification_items,viewGroup,false);

        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Notification notification=mNotification.get(i);
        //ekhane notification e comment jacche
        viewHolder.text.setText(notification.getText());
        getUserInfo(viewHolder.image_profile,viewHolder.username,notification.getUserid());
        getPostImage(viewHolder.recipeimage, notification.getPostid());
/*        if(notification.isIspost()){
            viewHolder.recipeimage.setVisibility(View.VISIBLE);
            getPostImage(viewHolder.recipeimage,notification.getPostid());

        }else{
            viewHolder.recipeimage.setVisibility(View.GONE);
        }*/
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notification.isIspost()){
                    SharedPreferences.Editor editor=mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                    editor.putString("postid",notification.getPostid());
                    editor.apply();

                    //((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new PostDetailFragment()).commit();
                }else {
                    SharedPreferences.Editor editor=mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                    editor.putString("profileid",notification.getUserid());
                    editor.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new Profilefragement()).commit();

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image_profile,recipeimage;
        public TextView username,text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile=itemView.findViewById(R.id.image_profile);
            recipeimage=itemView.findViewById(R.id.recipe_image);
            username=itemView.findViewById(R.id.username);
            text=itemView.findViewById(R.id.comment);

        }
    }
    private void getUserInfo(final ImageView imageView, final TextView username, String publisherid){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users")
                .child(publisherid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl()).into(imageView);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void  getPostImage(final ImageView imageView, String postid){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Post Recipe")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post= dataSnapshot.getValue(Post.class);
               Glide.with(mContext).load(post.getRecipeimage()).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
