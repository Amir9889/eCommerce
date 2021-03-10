package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String categoryName;
    private Button addNewProductBtn;
    private ImageView addProductImage;
    private EditText edtProductName, edtProductDesc, edtProductPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        categoryName = getIntent().getExtras().get(AdminCategoryActivity.PUT_KEY).toString();

        initializeUIElements();
    }

    private void initializeUIElements() {
        addProductImage = findViewById(R.id.select_product_image);
        addNewProductBtn = findViewById(R.id.add_new_product);
        edtProductName = findViewById(R.id.product_name);
        edtProductDesc = findViewById(R.id.product_desc);
        edtProductPrice = findViewById(R.id.product_price);
    }
}