package com.example.travel.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.example.travel.Adapter.RecommendedAdapter;
import com.example.travel.Domain.ItemDomain;
import com.example.travel.R;
import com.example.travel.databinding.ActivitySecondBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class SecondActivity extends BaseActivity {
    ActivitySecondBinding binding;
    String selectedCurrency = "LKR"; // Default currency

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    TextView name,email;
    Button signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        googleAuthuntication();

        binding.backBtn.setOnClickListener(view -> finish());

        binding.currencyLayout.setOnClickListener(view -> showCurrencyPopup());
    }

    private void showCurrencyPopup() {
        // Inflate the popup_currency layout
        View popupView = getLayoutInflater().inflate(R.layout.popup_currency, null);

        // Create a PopupWindow
        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);

        // Get the RadioGroup from the popup view
        RadioGroup currencyRadioGroup = popupView.findViewById(R.id.currencyRadioGroup1);

        // Access SharedPreferences to check current currency setting
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String currentCurrency = prefs.getString("selected_currency", "LKR");

        // Set the default selection based on the current currency
        RadioButton radioLKR = popupView.findViewById(R.id.radioLKR);
        RadioButton radioUSD = popupView.findViewById(R.id.radioUSD);

        if ("USD".equals(currentCurrency)) {
            radioUSD.setChecked(true);
        } else {
            radioLKR.setChecked(true);
        }

        // Set a listener for currency selection
        currencyRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedCurrency = "LKR"; // Default to LKR
            if (checkedId == R.id.radioUSD) {
                selectedCurrency = "USD";
            }

            // Update the currency and pass it to DetailActivity
            updateCurrency(selectedCurrency);
            popupWindow.dismiss();
        });
    }

    private void updateCurrency(String currency) {
        // Save the selected currency to SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("selected_currency", currency);
        editor.apply();

        // Pass the selected currency to DetailActivity
        Intent intent = new Intent(SecondActivity.this, DetailActivity.class);
        intent.putExtra("currency", currency);
        startActivity(intent);
    }

    private void googleAuthuntication() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        signOutBtn = findViewById(R.id.googleBtn);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct!=null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            name.setText(personName);
            email.setText(personEmail);
        }
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent( SecondActivity.this, ProfileActivity.class));
            }
        });
    }
}