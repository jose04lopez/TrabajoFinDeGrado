package com.principal.trabajofindegrado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddHabitActivity extends AppCompatActivity {

    private ImageButton btnToday, btnAdd, btnStatistics, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        // Referencias a los botones de la barra inferior
        btnToday = findViewById(R.id.btnToday);
        btnAdd = findViewById(R.id.btnAdd);
        btnStatistics = findViewById(R.id.btnStatistics);

        btnToday.setOnClickListener(v -> {
            Intent intent = new Intent(AddHabitActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para abrir la actividad de añadir hábito
                // Puedes iniciar una nueva actividad aquí usando un Intent
                Intent intent = new Intent(AddHabitActivity.this, AddHabitActivity.class);
                startActivity(intent);
            }
        });


    }
}

