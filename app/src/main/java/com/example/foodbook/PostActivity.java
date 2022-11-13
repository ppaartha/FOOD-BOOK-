package com.example.foodbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    Uri imageUri;
    String myUri="";
    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView cancel,recipe_image_added;
    TextView post_recipe;
    EditText recipe_name,description,ingredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        cancel=findViewById(R.id.cancel);
        recipe_image_added=findViewById(R.id.recipe_image_added);
        post_recipe=findViewById(R.id.post_recipe);
        recipe_name=findViewById(R.id.recipe_name);
        description=findViewById(R.id.description);
        ingredients=findViewById(R.id.ingredients);

        storageReference = FirebaseStorage.getInstance().getReference("Post Recipe");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this,MainActivity.class));
                finish();
            }
        });
        post_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadRecipe();
            }
        });

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(PostActivity.this);

    }
    private String getFileExtensis(Uri uri){
        ContentResolver contentResolver =getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadRecipe(){
        final ProgressDialog progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();
        if(imageUri!=null){
            final StorageReference filereference =storageReference.child(System.currentTimeMillis()
            + "."+ getFileExtensis(imageUri));
            uploadTask =filereference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                   if(!task.isComplete()){
                       throw task.getException();

                   }
                   return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri= task.getResult();
                        myUri =downloadUri.toString();

                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Post Recipe");
                        String postid=reference.push().getKey();
                        HashMap<String,Object> hashMap =new HashMap<>();
                        hashMap.put("postid",postid);
                        hashMap.put("recipename",recipe_name.getText().toString());
                        hashMap.put("recipeimage",myUri);
                        hashMap.put("description",description.getText().toString());
                        hashMap.put("ingredients",ingredients.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reference.child(postid).setValue(hashMap);

                        progressDialog.dismiss();
                        
                        startActivity(new Intent(PostActivity.this,MainActivity.class));
                        finish();
                        
                    }else {
                        Toast.makeText(PostActivity.this, "Failed!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri =result.getUri();
            recipe_image_added.setImageURI(imageUri);
        }else{
            Toast.makeText(this, "Something gone wrong!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this,MainActivity.class));
            finish();
        }
    }
}
