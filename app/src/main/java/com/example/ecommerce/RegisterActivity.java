package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private AppCompatButton createAccountBtn;
    private EditText edtName, edtPhone, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeUIElements();

    }

    private void initializeUIElements() {
        createAccountBtn = findViewById(R.id.register_btn);
        edtName = findViewById(R.id.register_username);
        edtPhone = findViewById(R.id.register_edt_phone_number);
        edtPassword = findViewById(R.id.register_edt_password);
    }
}