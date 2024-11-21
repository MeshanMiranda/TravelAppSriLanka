package com.example.travel.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.travel.Domain.ItemDomain;
import com.example.travel.R;
import com.example.travel.databinding.ActivityProfileBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ProfileActivity extends BaseActivity {
    ActivityProfileBinding binding;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn, profilePhoto, meshanProfile;
    TextView name, email;
    Button signOutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setItemSelected(R.id.profileBottom, true);
        binding.backBtn.setOnClickListener(view -> finish());
        binding.currencyLayout.setOnClickListener(view -> showCurrencyPopup());

        googleAuthuntication();
        googleBtn = findViewById(R.id.googleBtn);
        googleBtn.setOnClickListener(view -> signIn());
        bottomMenu();
    }

    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isSignedIn", true);
                editor.apply();

                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void googleAuthuntication() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        signOutBtn = findViewById(R.id.signOutBtn);
        googleBtn = findViewById(R.id.googleBtn);
        profilePhoto = findViewById(R.id.profilePhoto);
        meshanProfile = findViewById(R.id.meshanProfile);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            name.setText(personName);
            email.setText(personEmail);

            profilePhoto.setVisibility(View.GONE);
            meshanProfile.setVisibility(View.VISIBLE);

            googleBtn.setVisibility(View.GONE);
            signOutBtn.setVisibility(View.VISIBLE);

        } else {
            profilePhoto.setVisibility(View.VISIBLE);
            meshanProfile.setVisibility(View.GONE);

            googleBtn.setVisibility(View.VISIBLE);
            signOutBtn.setVisibility(View.GONE);
        }

        if (signOutBtn != null) {
            signOutBtn.setOnClickListener(view -> signOut());
        }
    }

    void signOut() {
        gsc.signOut().addOnCompleteListener(task -> {
            googleBtn.setVisibility(View.VISIBLE);
            signOutBtn.setVisibility(View.GONE);
            profilePhoto.setVisibility(View.VISIBLE);
            meshanProfile.setVisibility(View.GONE);
            name.setText("Sign in / Sign up");
            email.setText("example@gmail.com");

            SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isSignedIn", false);
            editor.apply();
        });

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
        Intent intent = new Intent(ProfileActivity.this, DetailActivity.class);
        intent.putExtra("currency", currency);
        startActivity(intent);
    }

    private void bottomMenu() {
        binding.bottomNavigation.setOnItemSelectedListener(itemId -> {
            if (itemId == R.id.explorer) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.browse) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.thrillophilia.com/places-to-visit-in-sri-lanka"));
                startActivity(intent);
            }
        });
    }
}
