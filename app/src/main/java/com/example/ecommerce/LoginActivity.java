package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.User;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText edtPhone, edtPassword;
    private AppCompatButton loginBtn;
    private ProgressDialog loadingBar;
    private CheckBox rememberMe;
    private TextView adminLink, notAdminLink;
    private static String parentDBName = "Users";

    public static void autoCompleteEditTexts(String phone, String password) {
        LoginActivity loginActivity = new LoginActivity();
        loginActivity.edtPhone.setText(phone);
        loginActivity.edtPassword.setText(password);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeUIElements();

        Paper.init(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDBName = "Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDBName = "Users";
            }
        });

    }

    private void loginUser() {
        String phone = edtPhone.getText().toString();
        String password = edtPassword.getText().toString();

        if (TextUtils.isEmpty(phone)){
            edtPhone.setError("Please enter your Phone Number!");
        }else if (TextUtils.isEmpty(password)){
            edtPassword.setError("Please enter your Password!");
        }else {
            loadingBar.setTitle("Login into Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(phone, password);
        }
    }

    private void allowAccessToAccount(String phone, String password) {

        if (rememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPassword, password);
        }

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.child(parentDBName).child(phone).exists())){
                    // Allow user to Login
                    User userData = snapshot.child(parentDBName).child(phone).getValue(User.class);
                    if (userData.getPhone().equals(phone)){
                        if (userData.getPassword().equals(password)){
                            // Specify the client is an admin or a client
                            if (parentDBName.equals("Admins")){
                                // The client is an admin
                                Toast.makeText(LoginActivity.this, "Welcome admin, you are logged in successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(getApplicationContext(), AdminCategoryActivity.class);
                                startActivity(intent);
                                finish();
                            }else if (parentDBName.equals("Users")){
                                // The client is a user
                                Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }else {
                            // Wrong password
                            edtPassword.setError("Wrong password.");
                            loadingBar.dismiss();
                        }
                    }else {
                        // Wrong phone number
                        edtPhone.setError("Wrong phone number");
                        loadingBar.dismiss();
                    }
                }else {
                    // Please create an account
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " number don't exist!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeUIElements() {
        edtPhone = findViewById(R.id.login_edt_phone_number);
        edtPassword = findViewById(R.id.login_edt_password);
        loginBtn = findViewById(R.id.login_btn);
        loadingBar = new ProgressDialog(this);
        rememberMe = findViewById(R.id.remember_me);
        adminLink = findViewById(R.id.admin_panel);
        notAdminLink = findViewById(R.id.not_admin_panel);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}