package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private AppCompatButton createAccountBtn;
    private EditText edtName, edtPhone, edtPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeUIElements();

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Get the information
                    createAnAccount();
            }
        });

    }

    private void createAnAccount() {
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String password = edtPassword.getText().toString();

        // Check the EditTexts to don't be empty
        if (TextUtils.isEmpty(name)){
            edtName.setError("Please enter your Name!");
        }else if (TextUtils.isEmpty(phone)){
            edtPhone.setError("Please enter your Phone Number!");
        }else if (TextUtils.isEmpty(password)){
            edtPassword.setError("Please enter your Password!");
        }else {
            // Everything is ok, so we store the data into Firebase Database !!!
            loadingBar.setTitle("Creating An Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            // Validation the user's phone number to check do we have an account on this phone number or not!?
            validatePhoneNumber(name, phone, password);
        }
    }

    private void validatePhoneNumber(String name, String phone, String password) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phone).exists())){
                    // The phone number dos'nt exist so we creating an account
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone", phone);
                    userDataMap.put("password", password);
                    userDataMap.put("name", name);
                    rootRef.child("Users").child(phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        intent.putExtra("PHONE", phone);
                                        intent.putExtra("PASSWORD", password);
                                        startActivity(intent);
                                    }else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    // This phone number is already registered.
                    Toast.makeText(RegisterActivity.this, "This" + phone + "already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "PLease try again using another phone number!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeUIElements() {
        createAccountBtn = findViewById(R.id.register_btn);
        edtName = findViewById(R.id.register_username);
        edtPhone = findViewById(R.id.login_edt_phone_number);
        edtPassword = findViewById(R.id.login_edt_password);
        loadingBar = new ProgressDialog(this);
    }
}