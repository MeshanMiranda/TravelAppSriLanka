package com.example.travel.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.example.travel.Domain.ItemDomain;
import com.example.travel.R;
import com.example.travel.databinding.ActivityDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String currencyFromIntent = getIntent().getStringExtra("currency");

        selectedCurrency = (currencyFromIntent != null) ? currencyFromIntent : prefs.getString("selected_currency", "LKR");
    }



        private void setVariable() {
            updatePrice();
            binding.titleTxt.setText(object.getTitle());
            binding.backBtn.setOnClickListener(v -> finish());
            binding.distanceTxt.setText(object.getDistance());
            binding.descriptionTxt.setText(object.getDescription());
            binding.addressTxt.setText(object.getAddress());
            binding.ratingTxt.setText(object.getScore() + " Rating");
            binding.ratingBar.setRating((float) object.getScore());

            Glide.with(DetailActivity.this)
                    .load(object.getPic())
                    .into(binding.pic);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Popular");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot locationSnapshot : snapshot.getChildren()) {
                        String title = locationSnapshot.child("title").getValue(String.class);
                        if (title != null && title.equals(object.getTitle())) {
                            String locationURL = locationSnapshot.child("URL").getValue(String.class);
                            if (locationURL != null && !locationURL.isEmpty()) {
                                binding.directionTxt.setText(R.string.direction);
                                binding.direction.setOnClickListener(v -> {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationURL));
                                    startActivity(intent);
                                });
                            } else {
                                binding.directionTxt.setText(R.string.no_direction_available);
                                binding.directionTxt.setClickable(false);
                            }
                            // Fetch and display weather information
                            String weather = locationSnapshot.child("weather").getValue(String.class);
                            updateWeather(weather);

                            String temp = locationSnapshot.child("temp").getValue(String.class);
                            updateTemperature(temp);

                            break;

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("DetailActivity", "Error fetching location URL: " + error.getMessage());
                    binding.directionTxt.setText(R.string.error_loading_direction);
                    binding.directionTxt.setClickable(false);

                }
            });


            binding.addToCartBtn.setOnClickListener(v -> {
                Intent intent = new Intent(DetailActivity.this, PaymentActivity.class);
                intent.putExtra("object", object);
                startActivity(intent);
            });
        }

    private void updateWeather(String weather) {
        if (weather != null) {


            int weatherIconResId;
            switch (weather.toLowerCase()) {
                case "sunny":
                    weatherIconResId = R.drawable.ic_sunny; // Replace with your sunny icon resource
                    break;
                case "rainy":
                    weatherIconResId = R.drawable.ic_rainy; // Replace with your rainy icon resource
                    break;
                case "cloudy":
                    weatherIconResId = R.drawable.ic_cloudy; // Replace with your cloudy icon resource
                    break;

                default:
                    weatherIconResId = R.drawable.ic_default_weather; // Default icon
                    break;
            }
            binding.weatherIcon.setImageResource(weatherIconResId);
        } else {
            binding.weatherTxt.setText(R.string.no_weather_data); // Set to "No weather data available" string resource
            binding.weatherIcon.setImageResource(R.drawable.ic_default_weather); // Default icon
        }
    }

    private void updateTemperature(String temp) {
        if (temp != null) {
            binding.celciusTxt.setText(temp+ "°C");
        } else {
            binding.celciusTxt.setText(R.string.no_temp_data);
        }
    }

    private void updatePrice() {
            double price = object.getPrice();
            if ("USD".equals(selectedCurrency)) {
                price = convert(price);
                binding.priceTxt.setText("USD " + String.format("%.2f", price));
            } else {
                binding.priceTxt.setText("LKR " + String.format("%.0f", price));
            }
        }

    private double convert(double lkrPrice){
        double exchangeRate = 0.0034; // LKR to USD
        return lkrPrice * exchangeRate;
    }

}

