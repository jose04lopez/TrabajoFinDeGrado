package com.principal.trabajofindegrado.Principal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.principal.trabajofindegrado.Adaptadores.CustomAdapter;
import com.principal.trabajofindegrado.Database.MyDatabaseHelper;
import com.principal.trabajofindegrado.Objetos.Habit;
import com.principal.trabajofindegrado.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * MainActivity es la actividad principal de la aplicación que muestra los hábitos del usuario y permite interactuar con ellos.
 *
 *  @author Jose y Guillermo
 *  @version 1.0
 */
public class MainActivity extends AppCompatActivity implements CustomAdapter.OnHabitClickListener {

    private CustomAdapter customAdapter;
    private ArrayList<Habit> habitList;
    private MyDatabaseHelper databaseHelper;
    private String userId, username;

    /**
     * Método llamado cuando se crea la actividad. Configura la interfaz de usuario y
     * establece los listeners de clic para los botones de navegación.
     *
     * @param savedInstanceState Objeto Bundle que contiene el estado previamente guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener información del usuario de la actividad anterior
        userId = getIntent().getStringExtra("USER_ID");
        username = getIntent().getStringExtra("USERNAME");

        if (userId != null) {
            // Configurar RecyclerView para mostrar hábitos
            RecyclerView habitsRecyclerView = findViewById(R.id.habitsRecyclerView);
            habitList = new ArrayList<>();
            databaseHelper = new MyDatabaseHelper(this);

            // Restablecer el estado de las casillas de verificación a medianoche
            resetCheckboxStatusAtMidnight();
            // Leer hábitos de la base de datos
            readHabitsFromDatabase();

            // Configurar Adaptador personalizado para el RecyclerView
            customAdapter = new CustomAdapter(this, habitList);
            customAdapter.setOnHabitClickListener(this);
            customAdapter.setDatabaseHelper(databaseHelper);
            habitsRecyclerView.setAdapter(customAdapter);
            habitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Configurar Spinner para filtrar hábitos por frecuencia
            Spinner spinnerFilter = findViewById(R.id.spinnerFilter);
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                    R.array.filters, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFilter.setAdapter(spinnerAdapter);

            spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    updateHabitList(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // No hacer nada
                }
            });

            // Configurar botones de la barra de acción
            ImageButton btnSettings = findViewById(R.id.btnSettings);
            ImageButton btnToday = findViewById(R.id.btnToday);
            ImageButton btnAdd = findViewById(R.id.btnAdd);
            ImageButton btnStatistics = findViewById(R.id.btnStatistics);

            btnSettings.setOnClickListener(v -> openSettingsActivity());
            btnToday.setOnClickListener(v -> openMainActivity());
            btnAdd.setOnClickListener(v -> openAddHabitActivity());
            btnStatistics.setOnClickListener(v -> openStatisticsActivity());
        } else {
            Toast.makeText(this, "Error: No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Lee los hábitos del usuario de la base de datos y los agrega a la lista de hábitos.
     */
    private void readHabitsFromDatabase() {
        Cursor cursor = databaseHelper.readAllHabits(userId);
        if (cursor != null && cursor.getCount() > 0) {
            int habitIdIndex = cursor.getColumnIndex("habit_id");
            int habitNameIndex = cursor.getColumnIndex("habit_name");
            int difficultyIndex = cursor.getColumnIndex("difficulty");
            int frequencyIndex = cursor.getColumnIndex("frequency");
            int startDateIndex = cursor.getColumnIndex("start_date");
            int checkbox1Index = cursor.getColumnIndex("checkbox1_status");
            int checkbox2Index = cursor.getColumnIndex("checkbox2_status");
            int checkbox3Index = cursor.getColumnIndex("checkbox3_status");

            while (cursor.moveToNext()) {
                int habitId = cursor.getInt(habitIdIndex);
                String habitName = cursor.getString(habitNameIndex);
                String difficulty = cursor.getString(difficultyIndex);
                int frequency = cursor.getInt(frequencyIndex);
                String startDate = cursor.getString(startDateIndex);
                int checkbox1Status = cursor.getInt(checkbox1Index) == 1 ? 1 : 0;
                int checkbox2Status = cursor.getInt(checkbox2Index) == 1 ? 1 : 0;
                int checkbox3Status = cursor.getInt(checkbox3Index) == 1 ? 1 : 0;

                Habit habit = new Habit(habitId, habitName, difficulty, frequency, startDate);
                habit.setCheckbox1Status(checkbox1Status);
                habit.setCheckbox2Status(checkbox2Status);
                habit.setCheckbox3Status(checkbox3Status);
                habitList.add(habit);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * Actualiza la lista de hábitos según la frecuencia seleccionada en el Spinner.
     *
     * @param selectedFrequency La frecuencia seleccionada en el Spinner.
     */
    @SuppressLint("NotifyDataSetChanged")
    private void updateHabitList(int selectedFrequency) {
        habitList.clear();

        if (selectedFrequency == 0) { // Si se selecciona "Todos"
            readHabitsFromDatabase(); // Cargar todos los hábitos
        } else {
            Cursor cursor = databaseHelper.readHabitsByFrequency(userId, selectedFrequency);
            if (cursor != null && cursor.getCount() > 0) {
                int habitIdIndex = cursor.getColumnIndex("habit_id");
                int habitNameIndex = cursor.getColumnIndex("habit_name");
                int difficultyIndex = cursor.getColumnIndex("difficulty");
                int frequencyIndex = cursor.getColumnIndex("frequency");
                int startDateIndex = cursor.getColumnIndex("start_date");
                int checkbox1Index = cursor.getColumnIndex("checkbox1_status");
                int checkbox2Index = cursor.getColumnIndex("checkbox2_status");
                int checkbox3Index = cursor.getColumnIndex("checkbox3_status");

                while (cursor.moveToNext()) {
                    int habitId = cursor.getInt(habitIdIndex);
                    String habitName = cursor.getString(habitNameIndex);
                    String difficulty = cursor.getString(difficultyIndex);
                    String startDate = cursor.getString(startDateIndex);
                    int checkbox1Status = cursor.getInt(checkbox1Index) == 1 ? 1 : 0;
                    int checkbox2Status = cursor.getInt(checkbox2Index) == 1 ? 1 : 0;
                    int checkbox3Status = cursor.getInt(checkbox3Index) == 1 ? 1 : 0;

                    Habit habit = new Habit(habitId, habitName, difficulty, selectedFrequency, startDate);
                    habit.setCheckbox1Status(checkbox1Status);
                    habit.setCheckbox2Status(checkbox2Status);
                    habit.setCheckbox3Status(checkbox3Status);
                    habitList.add(habit);
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        }

        customAdapter.notifyDataSetChanged();
    }


    private void openSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void openMainActivity() {
        Toast.makeText(this, "Ya estás en la actividad principal", Toast.LENGTH_SHORT).show();

    }

    private void openAddHabitActivity() {
        Intent intent = new Intent(MainActivity.this, AddHabitActivity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void openStatisticsActivity() {
        Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    @Override
    public void onHabitClick(int position) {
        if (position >= 0 && position < habitList.size()) {
            Habit selectedHabit = habitList.get(position);
            showEditOrDeleteDialog(selectedHabit);
        }
    }

    // Métodos para abrir actividades adicionales omitidos por brevedad

    /**
     * Muestra un cuadro de diálogo para editar o eliminar un hábito cuando se hace clic en él.
     *
     * @param habit El hábito seleccionado.
     */
    @SuppressLint("NotifyDataSetChanged")
    private void showEditOrDeleteDialog(Habit habit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(habit.getName());
        builder.setMessage("Selecciona una opción");
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

    /**
     * Muestra un cuadro de diálogo para editar un hábito cuando se selecciona la opción de edición.
     *
     * @param habit El hábito seleccionado.
     */
    @SuppressLint("NotifyDataSetChanged")
    private void showEditHabitDialog(Habit habit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Hábito");
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_habit, null);
        EditText editHabitName = view.findViewById(R.id.editHabitName);
        Spinner difficultySpinner = view.findViewById(R.id.difficultySpinner);
        Spinner frequencySpinner = view.findViewById(R.id.frequencySpinner);
        EditText editStartDate = view.findViewById(R.id.editStartDate);

        // Configuración del Spinner de dificultad
        List<String> difficultyOptions = Arrays.asList("Difícil", "Moderada", "Fácil");
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficultyOptions);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);
        difficultySpinner.setSelection(difficultyOptions.indexOf(habit.getDifficulty()));

        // Configuración del Spinner de frecuencia
        List<String> frequencyOptions = Arrays.asList("1", "2", "3");
        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frequencyOptions);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(frequencyAdapter);
        frequencySpinner.setSelection(habit.getFrequency() - 1); // Restar 1 para que el índice sea correcto

        editHabitName.setText(habit.getName());
        editStartDate.setText(habit.getStartDate());
        builder.setView(view);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String updatedHabitName = editHabitName.getText().toString().trim();
            String updatedDifficulty = difficultySpinner.getSelectedItem().toString();
            int updatedFrequency = Integer.parseInt(frequencySpinner.getSelectedItem().toString());
            String updatedStartDate = editStartDate.getText().toString().trim();

            if (!isValidDate(updatedStartDate)) {
                Toast.makeText(this, "El formato de la fecha debe ser YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                return;
            }

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



    /**
     * Verifica si la cadena dada es una fecha válida en formato YYYY-MM-DD.
     *
     * @param date La fecha a verificar.
     * @return true si la fecha es válida, de lo contrario false.
     */
    private boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    /**
     * Restablece el estado de las casillas de verificación a medianoche.
     */
    private void resetCheckboxStatusAtMidnight() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);

        if (hour == 0 && minute == 0 && second == 0) {
            databaseHelper.resetCheckboxStatus();
        }
    }
}
