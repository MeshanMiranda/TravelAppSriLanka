package com.example.travel.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travel.Domain.ItemDomain;
import com.example.travel.R;
import com.example.travel.databinding.ActivityPaymentBinding;
import com.example.travel.databinding.ActivityTicketBinding;

public class PaymentActivity extends BaseActivity {
    ActivityPaymentBinding binding;
    private ItemDomain object;
    private String selectedCurrency = "LKR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void getIntentExtra() {
        object = (ItemDomain) getIntent().getSerializableExtra("object");
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());
        updatePrice();
        binding.payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PaymentActivity.this, "Payment is Completed.", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(PaymentActivity.this, TicketActivity.class);
                        intent.putExtra("object", object);
                        startActivity(intent);
                    }
                }, 4000);
            }
        });

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