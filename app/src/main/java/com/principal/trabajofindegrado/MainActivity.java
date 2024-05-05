package com.principal.trabajofindegrado;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CustomAdapter.OnHabitClickListener {

    private CustomAdapter customAdapter;
    private ArrayList<Habit> habitList;
    private MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar variables y vistas
        RecyclerView habitsRecyclerView = findViewById(R.id.habitsRecyclerView);
        habitList = new ArrayList<>();
        databaseHelper = new MyDatabaseHelper(this);

        // Leer los hábitos de la base de datos y mostrarlos en el RecyclerView
        readHabitsFromDatabase();

        // Configurar el adaptador para el RecyclerView
        customAdapter = new CustomAdapter(this, habitList);
        customAdapter.setOnHabitClickListener(this);
        habitsRecyclerView.setAdapter(customAdapter);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Referencias a los botones de la barra inferior
        ImageButton btnSettings = findViewById(R.id.btnSettings);
        ImageButton btnToday = findViewById(R.id.btnToday);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ImageButton btnStatistics = findViewById(R.id.btnStatistics);

        // Agregar escuchadores de clic para los botones
        btnSettings.setOnClickListener(v -> openSettingsActivity());
        btnToday.setOnClickListener(v -> restartActivity());
        btnAdd.setOnClickListener(v -> openAddHabitActivity());
        btnStatistics.setOnClickListener(v -> openStatisticsActivity());
    }


    // Método para obtener los hábitos de la base de datos y llenar la lista
    private void readHabitsFromDatabase() {
        Cursor cursor = databaseHelper.readAllHabits();
        if (cursor != null && cursor.getCount() > 0) {
            int habitIdIndex = cursor.getColumnIndex("habit_id");
            int habitNameIndex = cursor.getColumnIndex("habit_name");
            int difficultyIndex = cursor.getColumnIndex("difficulty");
            int frequencyIndex = cursor.getColumnIndex("frequency");
            int startDateIndex = cursor.getColumnIndex("start_date");
            int completionStatusIndex = cursor.getColumnIndex("completion_status");

            while (cursor.moveToNext()) {
                int habitId = cursor.getInt(habitIdIndex);
                String habitName = cursor.getString(habitNameIndex);
                String difficulty = cursor.getString(difficultyIndex);
                int frequency = cursor.getInt(frequencyIndex);
                String startDate = cursor.getString(startDateIndex);
                boolean completionStatus = cursor.getInt(completionStatusIndex) == 1;

                Habit habit = new Habit(habitId, habitName, difficulty, frequency, startDate, completionStatus);
                habitList.add(habit);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    // Método para abrir la actividad de ajustes
    private void openSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    // Método para reiniciar la actividad
    private void restartActivity() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Método para abrir la actividad de añadir hábito
    private void openAddHabitActivity() {
        Intent intent = new Intent(MainActivity.this, AddHabitActivity.class);
        startActivity(intent);
    }

    // Método para abrir la actividad de estadísticas
    private void openStatisticsActivity() {
        Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
        startActivity(intent);
    }

    // Método para manejar el clic en un hábito del RecyclerView
    @Override
    public void onHabitClick(int position) {
        if (position >= 0 && position < habitList.size()) {
            Habit selectedHabit = habitList.get(position);
            showEditOrDeleteDialog(selectedHabit);
        }
    }

    // Método para mostrar un diálogo para editar o eliminar un hábito
    @SuppressLint("NotifyDataSetChanged")
    private void showEditOrDeleteDialog(Habit habit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(habit.getName());
        builder.setMessage("Selecciona una opción:");
        builder.setPositiveButton("Editar", (dialog, which) -> showEditHabitDialog(habit));
        builder.setNegativeButton("Eliminar", (dialog, which) -> {
            databaseHelper.deleteHabit(String.valueOf(habit.getId()));
            habitList.remove(habit);
            customAdapter.notifyDataSetChanged();
        });
        builder.setNeutralButton("Cancelar", (dialog, which) -> {
            // No hacer nada si se cancela
        });
        builder.create().show();
    }

    // Método para mostrar un diálogo para editar un hábito
    @SuppressLint("NotifyDataSetChanged")
    private void showEditHabitDialog(Habit habit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Hábito");
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_habit, null);
        EditText editHabitName = view.findViewById(R.id.editHabitName);
        EditText editDifficulty = view.findViewById(R.id.editDifficulty);
        EditText editFrequency = view.findViewById(R.id.editFrequency);
        EditText editStartDate = view.findViewById(R.id.editStartDate);
        editHabitName.setText(habit.getName());
        editDifficulty.setText(habit.getDifficulty());
        editFrequency.setText(String.valueOf(habit.getFrequency()));
        editStartDate.setText(habit.getStartDate());
        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String updatedHabitName = editHabitName.getText().toString().trim();
            String updatedDifficulty = editDifficulty.getText().toString().trim();
            int updatedFrequency = Integer.parseInt(editFrequency.getText().toString().trim());
            String updatedStartDate = editStartDate.getText().toString().trim();
            habit.setName(updatedHabitName);
            habit.setDifficulty(updatedDifficulty);
            habit.setFrequency(updatedFrequency);
            habit.setStartDate(updatedStartDate);
            databaseHelper.updateHabit(habit);
            customAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            // No hacer nada si se cancela
        });
        builder.create().show();
    }
}
