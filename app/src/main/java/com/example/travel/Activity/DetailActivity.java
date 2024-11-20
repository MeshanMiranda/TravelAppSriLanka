package com.example.travel.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.travel.Domain.ItemDomain;
import com.example.travel.R;
import com.example.travel.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemDomain object;
    private String selectedCurrency = "LKR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void getIntentExtra() {
        object = (ItemDomain) getIntent().getSerializableExtra("object");

        // Retrieve the currency from Intent or SharedPreferences, default to LKR
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String currencyFromIntent = getIntent().getStringExtra("currency");

        // Use the currency from Intent if available, otherwise use SharedPreferences
        selectedCurrency = (currencyFromIntent != null) ? currencyFromIntent : prefs.getString("selected_currency", "LKR");
    }

    private void setVariable() {
        updatePrice();
        binding.titleTxt.setText(object.getTitle());
        binding.backBtn.setOnClickListener(v -> finish());
        binding.bedTxt.setText(String.valueOf(object.getBed()));
        binding.durationTxt.setText(object.getDuration());
        binding.distanceTxt.setText(object.getDistance());
        binding.descriptionTxt.setText(object.getDescription());
        binding.addressTxt.setText(object.getAddress());
        binding.ratingTxt.setText(object.getScore() + " Rating");
        binding.ratingBar.setRating((float) object.getScore());

        Glide.with(DetailActivity.this)
                .load(object.getPic())
                .into(binding.pic);

        binding.addToCartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, TicketActivity.class);
            intent.putExtra("object", object);
            startActivity(intent);
        });
    }

    private void updatePrice() {
        double price = object.getPrice();
        if ("USD".equals(selectedCurrency)) {
            price = convert(price);  // Renamed method to convert
            binding.priceTxt.setText("USD " + String.format("%.2f", price));
        } else {
            binding.priceTxt.setText("LKR " + price);
        }
    }

    private double convert(double lkrPrice) {
        final double exchangeRate = 0.0031;
        return lkrPrice * exchangeRate;
    }
}

