package com.example.travel.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travel.R;
import com.example.travel.databinding.ActivityIntroBinding;
import com.example.travel.databinding.ActivityNotificationBinding;

public class NotificationActivity extends BaseActivity {
    ActivityNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progressBarNotification.setVisibility(View.VISIBLE);

        BottomMenu();
    }
    private void BottomMenu() {
        binding.bottomNavigation.setOnItemSelectedListener(itemId -> {
            if (itemId == R.id.profileBottom) {
                Intent intent = new Intent(NotificationActivity.this, ProfileActivity.class);
                startActivity(intent);

            } else if (itemId == R.id.browse) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.thrillophilia.com/places-to-visit-in-sri-lanka"));
                startActivity(intent);
            }
            else if (itemId == R.id.explorer) {
                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}