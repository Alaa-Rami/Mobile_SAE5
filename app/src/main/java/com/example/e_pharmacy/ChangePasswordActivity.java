package com.example.e_pharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private FirebaseAuth authProfile;
    private EditText editTextPwdCurr , editTextPwdNew ;
    private TextView textViewAuthenticated ;
    private ProgressBar progressBar;
    private String userPwdCurr;
    private Button buttonChangePwd , buttonReAuthenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle("Change Password");

        editTextPwdNew= findViewById(R.id.editText_change_pwd_new);
        editTextPwdCurr=findViewById(R.id.editText_change_pwd_current);
        textViewAuthenticated=findViewById(R.id.textView_change_pwd_authenticated);
        progressBar=findViewById(R.id.progressBar);
        buttonReAuthenticate=findViewById(R.id.button_change_pwd_authenticate);
        buttonChangePwd=findViewById(R.id.button_change_pwd);

        editTextPwdNew.setEnabled(false);
        buttonChangePwd.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =authProfile.getCurrentUser();

        if (firebaseUser.equals("")){
            Toast.makeText(ChangePasswordActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ChangePasswordActivity.this,UserProfileActivity.class);
            startActivity(intent);
            finish();

        }else {
            reAuthenticateUser(firebaseUser);
        }
    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
         buttonReAuthenticate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 userPwdCurr=editTextPwdCurr.getText().toString();

                 if (TextUtils.isEmpty(userPwdCurr)){
                     Toast.makeText(ChangePasswordActivity.this,"password is required!",Toast.LENGTH_LONG).show();
                     editTextPwdCurr.setError("please enter your current password");
                     editTextPwdCurr.requestFocus();
                 }else {
                     progressBar.setVisibility(View.VISIBLE);
                     AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userPwdCurr);

                     firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()){
                                 progressBar.setVisibility(View.GONE);

                                 editTextPwdCurr.setEnabled(true);
                                 editTextPwdNew.setEnabled(true);
                                 buttonReAuthenticate.setEnabled(false);
                                 buttonChangePwd.setEnabled(true);

                                 textViewAuthenticated.setText("you are authenticated , you can change your password now");
                                 Toast.makeText(ChangePasswordActivity.this,"password has been verified , change your password now",Toast.LENGTH_LONG).show();

                                 buttonChangePwd.setBackgroundTintList(ContextCompat.getColorStateList(ChangePasswordActivity.this , R.color.dark_green));

                                 buttonChangePwd.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         changePwd(firebaseUser);
                                     }
                                 });

                             }else {
                                 try {
                                     throw task.getException();
                                 }catch (Exception e){
                                     Toast.makeText(ChangePasswordActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                 }
                             }
                             progressBar.setVisibility(View.GONE);
                         }
                     });
                 }
             }
         });
    }

    private void changePwd(FirebaseUser firebaseUser) {
        String userPwdNew= editTextPwdNew.getText().toString();
        if (TextUtils.isEmpty(userPwdNew)){
            Toast.makeText(ChangePasswordActivity.this,"new password required",Toast.LENGTH_LONG).show();
            editTextPwdNew.setError("please enter a new password");
            editTextPwdNew.requestFocus();

        } else if (userPwdCurr.matches(userPwdNew)) {
            Toast.makeText(ChangePasswordActivity.this,"new password cannot be the same as old password",Toast.LENGTH_LONG).show();
            editTextPwdNew.setError("please enter a new password");
            editTextPwdNew.requestFocus();
        }else {
            firebaseUser.updatePassword(userPwdNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChangePasswordActivity.this," password has been changed ",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ChangePasswordActivity.this,UserProfileActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(ChangePasswordActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
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
            Intent intent = new Intent(ChangePasswordActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_update_email) {
            Intent intent = new Intent(ChangePasswordActivity.this,UpdateEmailActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_settings) {
            Toast.makeText(ChangePasswordActivity.this,"",Toast.LENGTH_LONG).show();
        } else if (id==R.id.menu_change_password) {
            Intent intent = new Intent(ChangePasswordActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_delete_profile) {
            Intent intent = new Intent(ChangePasswordActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(ChangePasswordActivity.this,"Logged out",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);

            //clear stack to prevent user from coming back
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK) ;
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(ChangePasswordActivity.this,"Something went wrong !",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}