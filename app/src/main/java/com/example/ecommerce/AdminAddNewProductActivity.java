package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String categoryName, desc, price, pName, saveCurrentDate, saveCurrentTime;
    private Button addNewProductBtn;
    private ImageView addProductImage;
    private EditText edtProductName, edtProductDesc, edtProductPrice;
    private static final int galleryPick = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        categoryName = getIntent().getExtras().get(AdminCategoryActivity.PUT_KEY).toString();
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        initializeUIElements();

        addProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });

    }

    private void initializeUIElements() {
        addProductImage = findViewById(R.id.select_product_image);
        addNewProductBtn = findViewById(R.id.add_new_product);
        edtProductName = findViewById(R.id.product_name);
        edtProductDesc = findViewById(R.id.product_desc);
        edtProductPrice = findViewById(R.id.product_price);
        loadingBar = new ProgressDialog(this);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryPick && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            addProductImage.setImageURI(imageUri);
        }
    }

    private void validateProductData() {
           desc = edtProductDesc.getText().toString();
           pName = edtProductName.getText().toString();
           price = edtProductPrice.getText().toString();
           
           if (imageUri == null) {
               Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
           }else if (TextUtils.isEmpty(pName)){
               edtProductName.setError("Fill this blank!");
           }else if ((TextUtils.isEmpty(desc))){
               edtProductDesc.setError("Fill this blank!");
           }else if (TextUtils.isEmpty(price)){
               edtProductPrice.setError("Fill this blank!");
           }else {
               // Information is ready to store
               storeProductInformation();
           }
    }

    private void storeProductInformation() {
        Log.d("Ali", "WTF");
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin please waite, while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate +" "+ saveCurrentTime; // This is a key for each product

        StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg"); // Create a link of image and store it in the Firebase Storage

        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // There is an error to uploading the image
                String errorMessage = e.getMessage();
                Toast.makeText(AdminAddNewProductActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Image is uploaded successfully!
                Toast.makeText(AdminAddNewProductActivity.this, "Product image uploaded successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString(); // It's just an Uri of the image not the URL
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Getting product's image URL successfully...", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });


    }

    private void saveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", desc);
        productMap.put("image", downloadImageUrl);
        productMap.put("price", price);
        productMap.put("category", categoryName);
        productMap.put("pName", pName);

        productRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            // Product is added successfully...
                            Intent intent = new Intent(getApplicationContext(), AdminCategoryActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product is added successfully...", Toast.LENGTH_SHORT).show();
                        }else {
                            // There is an error to adding product in database
                            loadingBar.dismiss();
                            String errMsg = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + errMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}