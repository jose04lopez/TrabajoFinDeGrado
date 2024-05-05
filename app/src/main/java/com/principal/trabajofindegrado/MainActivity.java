package com.principal.trabajofindegrado;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.principal.trabajofindegrado.MyDatabaseHelper;
import com.principal.trabajofindegrado.StatisticsActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView habitsRecyclerView;
    private CustomAdapter customAdapter;
    private ArrayList<Habit> habitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        habitsRecyclerView = findViewById(R.id.habitsRecyclerView);
        habitList = new ArrayList<>();

        // Obtener y mostrar los hábitos almacenados en la base de datos
        readHabitsFromDatabase();

        // Configurar el adaptador para el RecyclerView
        customAdapter = new CustomAdapter(this, habitList);
        habitsRecyclerView.setAdapter(customAdapter);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Referencia al botón de ajustes
        ImageButton btnSettings = findViewById(R.id.btnSettings);

        // Agregar escuchador de clic para el botón de ajustes
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la pantalla de configuración
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Referencia a los botones de la barra inferior
        ImageButton btnToday = findViewById(R.id.btnToday);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ImageButton btnStatistics = findViewById(R.id.btnStatistics);

        // Agregar escuchadores de clic para los botones
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para abrir la actividad de añadir hábito
                // Puedes iniciar una nueva actividad aquí usando un Intent
                Intent intent = new Intent(MainActivity.this, AddHabitActivity.class);
                startActivity(intent);
            }
        });

        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para abrir la actividad de estadísticas
                // Puedes iniciar una nueva actividad aquí usando un Intent
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });
    }

    // Método para obtener los hábitos de la base de datos y llenar la lista
    private void readHabitsFromDatabase() {
        MyDatabaseHelper databaseHelper = new MyDatabaseHelper(this);
        Cursor cursor = databaseHelper.readAllHabits();

        if (cursor != null && cursor.getCount() > 0) {
            int habitIdIndex = cursor.getColumnIndex("habit_id");
            int habitNameIndex = cursor.getColumnIndex("habit_name");
            int difficultyIndex = cursor.getColumnIndex("difficulty");
            int frequencyIndex = cursor.getColumnIndex("frequency");
            int startDateIndex = cursor.getColumnIndex("start_date");

            while (cursor.moveToNext()) {
                int habitId = cursor.getInt(habitIdIndex);
                String habitName = cursor.getString(habitNameIndex);
                String difficulty = cursor.getString(difficultyIndex);
                int frequency = cursor.getInt(frequencyIndex);
                String startDate = cursor.getString(startDateIndex);

                // Crear un objeto Habit y agregarlo a la lista
                Habit habit = new Habit(habitId, habitName, difficulty, frequency, startDate);
                habitList.add(habit);
            }
        }

        // Cerrar el cursor después de usarlo
        cursor.close();
    }
}
