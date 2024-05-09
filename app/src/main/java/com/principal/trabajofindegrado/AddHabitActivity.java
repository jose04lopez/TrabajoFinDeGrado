package com.principal.trabajofindegrado;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddHabitActivity extends AppCompatActivity {
    MyDatabaseHelper databaseHelper;
    private String userId, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        databaseHelper = new MyDatabaseHelper(this);

        username = getIntent().getStringExtra("USERNAME");
        userId = getIntent().getStringExtra("USER_ID");

        ImageButton btnToday = findViewById(R.id.btnToday);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ImageButton btnStatistics = findViewById(R.id.btnStatistics);

        btnToday.setOnClickListener(v -> {
            Intent intent = new Intent(AddHabitActivity.this, MainActivity.class);

            intent.putExtra("USERNAME", username);
            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(AddHabitActivity.this, AddHabitActivity.class);

            intent.putExtra("USERNAME", username);
            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });

        btnStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(AddHabitActivity.this, StatisticsActivity.class);

            intent.putExtra("USERNAME", username);
            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });

        RelativeLayout habit1 = findViewById(R.id.habithelp1);
        RelativeLayout habit2 = findViewById(R.id.habithelp2);
        RelativeLayout habit3 = findViewById(R.id.habithelp3);
        RelativeLayout habit4 = findViewById(R.id.habithelp4);
        RelativeLayout habit5 = findViewById(R.id.habithelp5);
        RelativeLayout habitCustom = findViewById(R.id.habithelp6);

        habit1.setOnClickListener(v -> showConfirmationDialog("Ejercicio matutino"));
        habit2.setOnClickListener(v -> showConfirmationDialog("Meditación"));
        habit3.setOnClickListener(v -> showConfirmationDialog("Lectura"));
        habit4.setOnClickListener(v -> showConfirmationDialog("Beber más agua"));
        habit5.setOnClickListener(v -> showConfirmationDialog("Estiramiento"));
        habitCustom.setOnClickListener(v -> showCustomHabitDialog());
    }

    private void showConfirmationDialog(String habitName) {
        if (isHabitAdded(habitName)) {
            Toast.makeText(this, "El hábito ya está agregado", Toast.LENGTH_SHORT).show();
            return;
        }

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
                        case "Meditación":
                            selectedHabitLayout = findViewById(R.id.habithelp2);
                            difficultyTextView = selectedHabitLayout.findViewById(R.id.habithelp2_difficulty);
                            frequencyTextView = selectedHabitLayout.findViewById(R.id.habithelp2_frequency);
                            break;
                        case "Lectura":
                            selectedHabitLayout = findViewById(R.id.habithelp3);
                            difficultyTextView = selectedHabitLayout.findViewById(R.id.habithelp3_difficulty);
                            frequencyTextView = selectedHabitLayout.findViewById(R.id.habithelp3_frequency);
                            break;
                        case "Beber más agua":
                            selectedHabitLayout = findViewById(R.id.habithelp4);
                            difficultyTextView = selectedHabitLayout.findViewById(R.id.habithelp4_difficulty);
                            frequencyTextView = selectedHabitLayout.findViewById(R.id.habithelp4_frequency);
                            break;
                        case "Estiramiento":
                            selectedHabitLayout = findViewById(R.id.habithelp5);
                            difficultyTextView = selectedHabitLayout.findViewById(R.id.habithelp5_difficulty);
                            frequencyTextView = selectedHabitLayout.findViewById(R.id.habithelp5_frequency);
                            break;
                        default:
                            return;
                    }

                    if (difficultyTextView != null && frequencyTextView != null) {
                        String difficulty = difficultyTextView.getText().toString().replace("Dificultad: ", "");
                        int frequency = Integer.parseInt(frequencyTextView.getText().toString().replace("Frecuencia: ", ""));
                        databaseHelper.addHabit(userId, habitName, difficulty, frequency, getCurrentDate());

                    } else {
                        Toast.makeText(AddHabitActivity.this, "Error: Hábito no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showCustomHabitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_habit, null);
        builder.setView(dialogView);

        EditText habitNameEditText = dialogView.findViewById(R.id.editText_habit_name);
        Spinner difficultySpinner = dialogView.findViewById(R.id.spinner_difficulty);
        Spinner frequencySpinner = dialogView.findViewById(R.id.spinner_frequency);

        builder.setTitle("Diseña tu hábito")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    String habitName = habitNameEditText.getText().toString();
                    String difficulty = difficultySpinner.getSelectedItem().toString();
                    int frequency = frequencySpinner.getSelectedItemPosition() + 1;
                    databaseHelper.addHabit(userId, habitName, difficulty, frequency, getCurrentDate());
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private boolean isHabitAdded(String habitName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {MyDatabaseHelper.COLUMN_TITLE};
        String selection = MyDatabaseHelper.COLUMN_TITLE + " = ? AND " + MyDatabaseHelper.USER_COLUMN_ID + " = ?";
        String[] selectionArgs = {habitName, userId};

        Cursor cursor = db.query(MyDatabaseHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }
}
