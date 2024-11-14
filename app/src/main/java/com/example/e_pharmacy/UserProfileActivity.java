package com.example.e_pharmacy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome, textViewFullName, textViewEmail,textViewDob,textViewGender,textViewMobile;
    private ProgressBar progressBar;
    private  String fullnName,email,doB,gender,mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("Home");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeToRefresh();

        textViewWelcome=findViewById(R.id.textView_show_welcome);
        textViewFullName=findViewById(R.id.textView_show_full_name);
        textViewEmail=findViewById(R.id.textView_show_email);
        textViewDob=findViewById(R.id.textView_show_dob);
        textViewGender=findViewById(R.id.textView_show_gender);
        textViewMobile=findViewById(R.id.textView_show_mobile);
        progressBar= findViewById(R.id.progress_bar_profile_pic);

        // set onclicklistener on imageview to open UploadProfilePicActivity
        imageView = findViewById(R.id.imageView_profile_dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });

        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(UserProfileActivity.this,"Something went wrong ! user details are not available at the moment",Toast.LENGTH_LONG).show();

        }else {
            checkifEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
       loadProfileImage();

    }

    private void loadProfileImage() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfilePrefs", MODE_PRIVATE);
        String savedImageUri = sharedPreferences.getString("profile_image_uri", null);

        if (savedImageUri != null) {
            // Use Picasso to load the image from the URI
            Picasso.get().load(savedImageUri).into(imageView);
        } else {
            Toast.makeText(this, "No profile picture saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void swipeToRefresh() {
        swipeContainer = findViewById((R.id.swipeContainer));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(getIntent());
                finish();
                overridePendingTransition(0,0);
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    //user coming to userprofile after successful registration
    private void checkifEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        //set up alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("please verify your email now , you can not login without email verification next time");

        //Open email appsif user clicks continue
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //Create the alertDialog
        AlertDialog alertDialog = builder.create();

        //show the alertdialog
        alertDialog.show();
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //extracting user ref from db for regestired users
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                 if (readUserDetails != null){
                     fullnName= firebaseUser.getDisplayName();
                     email= firebaseUser.getEmail();
                     doB= readUserDetails.doB;
                     gender=readUserDetails.gender;
                     mobile=readUserDetails.mobile;

                     textViewWelcome.setText(getString(R.string.welcome_head_profile, fullnName));
                     textViewFullName.setText(fullnName);
                     textViewEmail.setText(email);
                     textViewDob.setText(doB);
                     textViewGender.setText(gender);
                     textViewMobile.setText(mobile);

                     // set user dp after upload
                     Uri uri = firebaseUser.getPhotoUrl();

                     Picasso.get().load(uri).into(imageView);

                 }else {
                     Toast.makeText(UserProfileActivity.this,"Something went wrong !",Toast.LENGTH_LONG).show();

                 }
                 progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this,"Something went wrong !",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    //creating actionbar menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
//when any item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id== android.R.id.home){
            NavUtils.navigateUpFromSameTask(UserProfileActivity.this);
        }
        else if (id==R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id==R.id.menu_update_profile) {
            Intent intent = new Intent(UserProfileActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_update_email) {
            Intent intent = new Intent(UserProfileActivity.this,UpdateEmailActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_settings) {
            Toast.makeText(UserProfileActivity.this,"",Toast.LENGTH_LONG).show();
        } else if (id==R.id.menu_change_password) {
            Intent intent = new Intent(UserProfileActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_delete_profile) {
            Intent intent = new Intent(UserProfileActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(UserProfileActivity.this,"Logged out",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);

            //clear stack to prevent user from coming back
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK) ;
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(UserProfileActivity.this,"Something went wrong !",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}