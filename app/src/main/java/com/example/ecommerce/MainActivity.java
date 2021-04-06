package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ecommerce.Model.User;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton joinNowBtn, loginBtn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUIElements();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        Paper.init(this);
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPassword);
        if (UserPasswordKey != null && UserPhoneKey != null){
            allowAccess(UserPhoneKey, UserPasswordKey);
            loadingBar.setTitle("Already Logged in");
            loadingBar.setMessage("Please wait.....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        }

    }

    private void allowAccess(String userPhoneKey, String userPasswordKey) {

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(userPhoneKey).exists()){
                    User user = snapshot.child("Users").child(userPhoneKey).getValue(User.class);
                    if (user.getPhone().equals(userPhoneKey)){
                        if (user.getPassword().equals(userPasswordKey)){
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Logged in successfully...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        loadingBar.dismiss();
                        Toast.makeText(MainActivity.this, "Wrong Number!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Account whit this " + userPhoneKey + " number don't exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initializeUIElements() {
        joinNowBtn = findViewById(R.id.main_join_now_btn);
        loginBtn = findViewById(R.id.login_btn);
        loadingBar = new ProgressDialog(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}