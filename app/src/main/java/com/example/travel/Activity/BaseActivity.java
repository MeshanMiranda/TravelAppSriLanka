package com.example.travel.Activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
    //https://firebasestorage.googleapis.com/v0/b/project189-ede2a.appspot.com/o/banner1.png?alt=media&token=f525f102-1a82-4bc3-b697-b9d3b2e85e26
    //https://firebasestorage.googleapis.com/v0/b/project189-ede2a.appspot.com/o/banner2.png?alt=media&token=898ca000-1f21-42df-84e1-e073ec464ae7
}