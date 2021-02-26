package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button joinNowBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUIElements();
    }

    private void initializeUIElements() {
        joinNowBtn = findViewById(R.id.main_join_now_btn);
        loginBtn = findViewById(R.id.main_login_btn);
    }
}