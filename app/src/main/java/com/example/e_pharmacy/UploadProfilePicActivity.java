package com.example.e_pharmacy;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadProfilePicActivity extends AppCompatActivity {

    private ProgressBar progressBar ;
    private ImageView imageViewUploadPic;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser ;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;
    private Button buttonSavePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_pic);

        getSupportActionBar().setTitle("Upload Profile Picture");

        Button buttonUploadPicChoose = findViewById(R.id.upload_pic_choose_button);
        buttonSavePic = findViewById(R.id.save_pic_button); // New save button
        progressBar = findViewById(R.id.progressBar);
        imageViewUploadPic = findViewById(R.id.imageView_profile_dp);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        // Load the user's profile picture if available
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfilePrefs", MODE_PRIVATE);
        String savedImageUri = sharedPreferences.getString("profile_image_uri", null);
        if (savedImageUri != null) {
            uriImage = Uri.parse(savedImageUri);
            imageViewUploadPic.setImageURI(uriImage);
        }

        buttonUploadPicChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        // Save button functionality
        buttonSavePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfileImage();
            }
        });

    }

    private void saveProfileImage() {
        if (uriImage != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserProfilePrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("profile_image_uri", uriImage.toString());
            editor.apply();

            progressBar.setVisibility(View.GONE);
            Toast.makeText(UploadProfilePicActivity.this, "Profile picture saved!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UploadProfilePicActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(UploadProfilePicActivity.this, "No file selected", Toast.LENGTH_LONG).show();
        }
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode== RESULT_OK && data!= null && data.getData() != null){
            uriImage = data.getData();
            imageViewUploadPic.setImageURI(uriImage);
            buttonSavePic.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //when any item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id==R.id.menu_update_profile) {
            Intent intent = new Intent(UploadProfilePicActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_update_email) {
            Intent intent = new Intent(UploadProfilePicActivity.this,UpdateEmailActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_settings) {
            Toast.makeText(UploadProfilePicActivity.this,"",Toast.LENGTH_LONG).show();
        } else if (id==R.id.menu_change_password) {
            Intent intent = new Intent(UploadProfilePicActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_delete_profile) {
            Intent intent = new Intent(UploadProfilePicActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(UploadProfilePicActivity.this,"Logged out",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UploadProfilePicActivity.this, MainActivity.class);

            //clear stack to prevent user from coming back
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK) ;
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(UploadProfilePicActivity.this,"Something went wrong !",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}