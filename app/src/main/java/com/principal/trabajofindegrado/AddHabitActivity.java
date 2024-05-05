package com.principal.trabajofindegrado;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    MyDatabaseHelper databaseHelper; // Declaración de objeto para la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        // Inicialización del objeto MyDatabaseHelper
        databaseHelper = new MyDatabaseHelper(this);

        // Referencias a los botones de la barra inferior
        ImageButton btnToday = findViewById(R.id.btnToday);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ImageButton btnStatistics = findViewById(R.id.btnStatistics);

        // Referencias a los TextView de los hábitos
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

            Intent intent = new Intent(AddHabitActivity.this, AddHabitActivity.class);
            startActivity(intent);
            finish();
        });

        // Listener para el botón Statistics
        btnStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(AddHabitActivity.this, StatisticsActivity.class);
            startActivity(intent);
            finish();
        });

        // Establecer OnClickListener para cada hábito
        habit1.setOnClickListener(v -> showConfirmationDialog("Ejercicio matutino"));
        habit2.setOnClickListener(v -> showConfirmationDialog("Meditación diaria"));
        habit3.setOnClickListener(v -> showConfirmationDialog("Lectura nocturna"));
        habit4.setOnClickListener(v -> showConfirmationDialog("Beber más agua"));
        habit5.setOnClickListener(v -> showConfirmationDialog("Estiramiento nocturno"));
        habitCustom.setOnClickListener(v -> showCustomHabitDialog());
    }

    // Método para mostrar un diálogo de confirmación para añadir un hábito
    private void showConfirmationDialog(String habitName) {
        // Verificar si el hábito ya está agregado
        if (isHabitAdded(habitName)) {
            Toast.makeText(this, "El hábito ya está agregado", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que quieres añadir el hábito '" + habitName + "'?")
                .setPositiveButton("Aceptar", (dialog, id) -> {
                    // Obtener referencias a los TextView de dificultad y frecuencia del hábito seleccionado
                    RelativeLayout selectedHabitLayout;
                    TextView difficultyTextView;
                    TextView frequencyTextView;
                    switch (habitName) {
                        // Asignar las referencias según el hábito seleccionado
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
                            return; // Manejar caso de hábito no reconocido
                    }

                    // Añadir el hábito a la base de datos
                    if (difficultyTextView != null && frequencyTextView != null) {
                        String difficulty = difficultyTextView.getText().toString().replace("Dificultad: ", "");
                        int frequency = Integer.parseInt(frequencyTextView.getText().toString().replace("Frecuencia: ", ""));
                        String startDate = getCurrentDate();
                        boolean completionStatus = false;
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
        Spinner frequencySpinner = dialogView.findViewById(R.id.spinner_frequency);

        builder.setTitle("Diseña tu hábito")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    // Obtener los datos del hábito personalizado y añadirlo a la base de datos
                    String habitName = habitNameEditText.getText().toString();
                    String difficulty = difficultySpinner.getSelectedItem().toString();
                    int frequency = frequencySpinner.getSelectedItemPosition() + 1;
                    databaseHelper.addHabit(habitName, difficulty, frequency, getCurrentDate(), false);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Método para obtener la fecha actual en el formato "yyyy-MM-dd"
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Método para verificar si el hábito ya está agregado a la base de datos
    private boolean isHabitAdded(String habitName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {MyDatabaseHelper.COLUMN_TITLE};
        String selection = MyDatabaseHelper.COLUMN_TITLE + " = ?";
        String[] selectionArgs = {habitName};

        Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }

}
