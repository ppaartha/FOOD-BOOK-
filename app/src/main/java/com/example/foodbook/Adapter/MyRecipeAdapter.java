package com.example.foodbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.foodbook.Model.Post;
import com.example.foodbook.R;

import java.util.List;

public class MyRecipeAdapter extends  RecyclerView.Adapter<MyRecipeAdapter.ViewHolder> {

    private Context context;
    private List<Post> mPosts;

    public MyRecipeAdapter(Context context,List<Post> mPosts){
        this.context=context;
        this.mPosts=mPosts;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(context).inflate(R.layout.recipes_item,viewGroup,false);


        return new MyRecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Post post=mPosts.get(i);
        Glide.with(context).load(post.getRecipeimage()).into(viewHolder.recipe_image);
    }

    @Override
    public int getItemCount() {

        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView recipe_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipe_image=itemView.findViewById(R.id.recipe_image);
        }
    }
}
