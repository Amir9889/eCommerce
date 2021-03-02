package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    public static final String PUT_KEY = "category" ;
    private ImageView tShirts, sportTShirts, femaleDresses, sweaters;
    private ImageView glasses, hatsCaps, walletBagsPurses, shoes;
    private ImageView headphonesHandFree, laptops, watches, mobilePhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        initialUIElements();


    }

    private void initialUIElements() {

        tShirts = findViewById(R.id.t_shirts);
        sportTShirts = findViewById(R.id.sports_t_shirts);
        femaleDresses = findViewById(R.id.female_dresses);
        sweaters = findViewById(R.id.sweaters);

        glasses = findViewById(R.id.glasses);
        hatsCaps = findViewById(R.id.hats_caps);
        walletBagsPurses = findViewById(R.id.purses_bags_wallets);
        shoes = findViewById(R.id.shoes);

        headphonesHandFree = findViewById(R.id.headphonens_handsfree);
        laptops = findViewById(R.id.laptops_pc);
        watches = findViewById(R.id.watches);
        mobilePhones = findViewById(R.id.mobiles);
    }

    public void goToAddProduct(View view) {
        Intent intent = new Intent(getApplicationContext(), AdminAddNewProductActivity.class);
        switch (view.getId()){

            case R.id.t_shirts :
                intent.putExtra(PUT_KEY, "tShirts");
                break;
            case R.id.sports_t_shirts :
                intent.putExtra(PUT_KEY, "Sports tShirts");
                break;
            case R.id.female_dresses :
                intent.putExtra(PUT_KEY, "Female Dresses");
                break;
            case R.id.sweaters :
                intent.putExtra(PUT_KEY, "Sweaters");
                break;


            case R.id.glasses :
                intent.putExtra(PUT_KEY, "Glasses");
                break;
            case R.id.hats_caps :
                intent.putExtra(PUT_KEY, "Hats Caps");
                break;
            case R.id.purses_bags_wallets :
                intent.putExtra(PUT_KEY, "Wallets Bags Purses");
                break;
            case R.id.shoes :
                intent.putExtra(PUT_KEY, "Shoes");
                break;


            case R.id.headphonens_handsfree:
                intent.putExtra(PUT_KEY, "HeadPhones HandFree");
                break;
            case R.id.laptops_pc:
                intent.putExtra(PUT_KEY, "Laptops");
                break;
            case R.id.watches:
                intent.putExtra(PUT_KEY, "Watches");
                break;
            case R.id.mobiles:
                intent.putExtra(PUT_KEY, "Mobile Phones");
                break;
        }

        startActivity(intent);
    }
}