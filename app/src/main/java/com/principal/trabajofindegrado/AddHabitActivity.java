package com.principal.trabajofindegrado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.app.AlertDialog;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddHabitActivity extends AppCompatActivity {
    MyDatabaseHelper databaseHelper = new MyDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);


        // Referencias a los botones de la barra inferior
        ImageButton btnToday = findViewById(R.id.btnToday);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ImageButton btnStatistics = findViewById(R.id.btnStatistics);

        // Obtener referencias a los TextView de los hábitos
        RelativeLayout habit1 = findViewById(R.id.habithelp1);
        RelativeLayout habit2 = findViewById(R.id.habithelp2);
        RelativeLayout habit3 = findViewById(R.id.habithelp3);
        RelativeLayout habit4 = findViewById(R.id.habithelp4);
        RelativeLayout habit5 = findViewById(R.id.habithelp5);

        RelativeLayout habitCustom = findViewById(R.id.habithelp6);


        // Listener para el botón Today
        btnToday.setOnClickListener(v -> {
            Intent intent = new Intent(AddHabitActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Listener para el botón Add
        btnAdd.setOnClickListener(v -> {
            // Lógica para abrir la actividad de añadir hábito
            // Puedes iniciar una nueva actividad aquí usando un Intent
            Intent intent = new Intent(AddHabitActivity.this, AddHabitActivity.class);
            startActivity(intent);
        });

        btnStatistics.setOnClickListener(v -> {
            // Lógica para abrir la actividad de estadísticas
            // Puedes iniciar una nueva actividad aquí usando un Intent
            Intent intent = new Intent(AddHabitActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        // Establecer OnClickListener para cada hábito
        habit1.setOnClickListener(v -> showConfirmationDialog("Ejercicio matutino"));

        habit2.setOnClickListener(v -> showConfirmationDialog("Meditación diaria"));

        habit3.setOnClickListener(v -> showConfirmationDialog("Lectura nocturna"));

        habit4.setOnClickListener(v -> showConfirmationDialog("Beber más agua"));

        habit5.setOnClickListener(v -> showConfirmationDialog("Estiramiento nocturno"));

        habitCustom.setOnClickListener(v -> showCustomHabitDialog());

    }

    private void showConfirmationDialog(String habitName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que quieres añadir el hábito '" + habitName + "'?")
                .setPositiveButton("Aceptar", (dialog, id) -> {
                    RelativeLayout selectedHabitLayout;
                    TextView difficultyTextView;
                    TextView frequencyTextView;

                    switch (habitName) {
                        case "Ejercicio matutino":
                            selectedHabitLayout = findViewById(R.id.habithelp1);
                            difficultyTextView = selectedHabitLayout.findViewById(R.id.habithelp1_difficulty);
                            frequencyTextView = selectedHabitLayout.findViewById(R.id.habithelp1_frequency);
                            break;
                        case "Meditación diaria":
                            selectedHabitLayout = findViewById(R.id.habithelp2);
                            difficultyTextView = selectedHabitLayout.findViewById(R.id.habithelp2_difficulty);
                            frequencyTextView = selectedHabitLayout.findViewById(R.id.habithelp2_frequency);
                            break;
                        case "Lectura nocturna":
                            selectedHabitLayout = findViewById(R.id.habithelp3);
                            difficultyTextView = selectedHabitLayout.findViewById(R.id.habithelp3_difficulty);
                            frequencyTextView = selectedHabitLayout.findViewById(R.id.habithelp3_frequency);
                            break;
                        case "Beber más agua":
                            selectedHabitLayout = findViewById(R.id.habithelp4);
                            difficultyTextView = selectedHabitLayout.findViewById(R.id.habithelp4_difficulty);
                            frequencyTextView = selectedHabitLayout.findViewById(R.id.habithelp4_frequency);
                            break;
                        case "Estiramiento nocturno":
                            selectedHabitLayout = findViewById(R.id.habithelp5);
                            difficultyTextView = selectedHabitLayout.findViewById(R.id.habithelp5_difficulty);
                            frequencyTextView = selectedHabitLayout.findViewById(R.id.habithelp5_frequency);
                            break;
                        default:
                            // Manejar caso de hábito no reconocido
                            return;
                    }

                    if (selectedHabitLayout != null && difficultyTextView != null && frequencyTextView != null) {
                        String difficulty = difficultyTextView.getText().toString().replace("Dificultad: ", "");
                        int frequency = Integer.parseInt(frequencyTextView.getText().toString().replace("Frecuencia: ", ""));
                        String startDate = getCurrentDate();
                        int completionStatus = 0;

                        databaseHelper.addHabit(habitName, difficulty, frequency, startDate, completionStatus);
                    } else {
                        Toast.makeText(AddHabitActivity.this, "Error: Hábito no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    // No hacer nada si se cancela
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // Método para mostrar el diálogo para diseñar un hábito personalizado
    private void showCustomHabitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_habit, null);
        builder.setView(dialogView);

        EditText habitNameEditText = dialogView.findViewById(R.id.editText_habit_name);
        Spinner difficultySpinner = dialogView.findViewById(R.id.spinner_difficulty);

        builder.setTitle("Diseña tu hábito")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    String habitName = habitNameEditText.getText().toString();
                    String difficulty = difficultySpinner.getSelectedItem().toString();
                    // Agrega lógica para guardar el hábito personalizado
                    // Por ejemplo, puedes usar SharedPreferences o una base de datos
                    Toast.makeText(AddHabitActivity.this, "Hábito '" + habitName + "' añadido", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Método para obtener la fecha actual en el formato deseado (por ejemplo, "yyyy-MM-dd")
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
}
