package com.principal.trabajofindegrado;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "canal_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Jose es gay")
                .setContentText("Mucho")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Switch switchNotifications = findViewById(R.id.switch_notifications);
        RelativeLayout rlHelp = findViewById(R.id.rl_help);
        ImageButton btnToday = findViewById(R.id.btnToday);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ImageButton btnStatistics = findViewById(R.id.btnStatistics);



        rlHelp.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);
            startActivity(intent);
        });

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AddHabitActivity.class);
            startActivity(intent);
        });

        btnToday.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "Nombre del canal";
                    String description = "Descripción del canal";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("canal_id", name, importance);
                    channel.setDescription(description);
                    notificationManager.createNotificationChannel(channel);
                    notificationManager.notify(1, builder.build());
                }



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

            btnAdd.setOnClickListener(v -> {
                Intent intent = new Intent(HelpActivity.this, AddHabitActivity.class);
                startActivity(intent);
            });
        }
    }


}
