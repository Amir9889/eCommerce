package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText edtPhone, edtPassword;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeUIElements();

        bundle = getIntent().getExtras();
        if (!bundle.isEmpty()){
            edtPhone.setText(bundle.getString("PHONE"));
            edtPassword.setText(bundle.getString("PASSWORD"));
        }

    }

    private void initializeUIElements() {
        edtPhone = findViewById(R.id.login_edt_phone_number);
        edtPassword = findViewById(R.id.login_edt_password);
    }
}