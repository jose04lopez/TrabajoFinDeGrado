package com.principal.trabajofindegrado;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch switchNotifications = findViewById(R.id.switch_notifications);
        RelativeLayout rlHelp = findViewById(R.id.rl_help);
        ImageButton btnToday = findViewById(R.id.btnToday);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ImageButton btnStatistics = findViewById(R.id.btnStatistics);



        rlHelp.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);
            startActivity(intent);
        });

        btnToday.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

            } else {

            }
        });
    }

    public static class HelpActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_help);

            ImageButton btnToday = findViewById(R.id.btnToday);
            ImageButton btnAdd = findViewById(R.id.btnAdd);
            ImageButton btnStatistics = findViewById(R.id.btnStatistics);

            btnToday.setOnClickListener(v -> {
                Intent intent = new Intent(HelpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }


}
