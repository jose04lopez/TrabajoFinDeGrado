package com.principal.trabajofindegrado.Principal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.principal.trabajofindegrado.Adaptadores.CustomAdapterStatistics;
import com.principal.trabajofindegrado.Database.MyDatabaseHelper;
import com.principal.trabajofindegrado.Objetos.Habit;
import com.principal.trabajofindegrado.R;

import java.util.ArrayList;

/**
 * Actividad que muestra las estadísticas de los hábitos del usuario.
 *
 * @author Jose y Guillermo
 * @version 1.0
 */
public class StatisticsActivity extends AppCompatActivity implements CustomAdapterStatistics.OnHabitClickListener {

    private CustomAdapterStatistics customAdapterStatistics;
    private ArrayList<Habit> habitList;
    private String userId, username;
    private MyDatabaseHelper databaseHelper;
    private TextView noHabitsTextView;


    /**
     * Método llamado cuando se crea la actividad.
     *
     * @param savedInstanceState Datos de estado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        databaseHelper = new MyDatabaseHelper(this);
        noHabitsTextView = findViewById(R.id.noHabitsTextView); // Obtener referencia al TextView

        userId = getIntent().getStringExtra("USER_ID");
        username = getIntent().getStringExtra("USERNAME");

        // Inicializar RecyclerView
        RecyclerView habitsRecyclerView = findViewById(R.id.recyclerViewHabits);
        habitList = new ArrayList<>();
        customAdapterStatistics = new CustomAdapterStatistics(this, habitList);
        customAdapterStatistics.setOnHabitClickListener(this);
        habitsRecyclerView.setAdapter(customAdapterStatistics);
        habitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton btnToday = findViewById(R.id.btnToday);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ImageButton btnStatistics = findViewById(R.id.btnStatistics);

        btnToday.setOnClickListener(v -> openMainActivity());
        btnAdd.setOnClickListener(v -> openAddHabitActivity());
        btnStatistics.setOnClickListener(v -> openStatisticsActivity());

        // Cargar los hábitos desde la base de datos
        loadHabitsFromDatabase();
    }

    /**
     * Carga los hábitos desde la base de datos y los muestra en la interfaz de usuario.
     */
    @SuppressLint("NotifyDataSetChanged")
    private void loadHabitsFromDatabase() {
        // Limpiar la lista de hábitos para evitar duplicados al recargar
        habitList.clear();

        // Obtener los hábitos del usuario actual desde la base de datos
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
            int daysCompleted = cursor.getColumnIndex("day_completed");

            // Recorrer el cursor y agregar cada hábito a la lista
            while (cursor.moveToNext()) {
                int habitId = cursor.getInt(habitIdIndex);
                String habitName = cursor.getString(habitNameIndex);
                String difficulty = cursor.getString(difficultyIndex);
                int frequency = cursor.getInt(frequencyIndex);
                String startDate = cursor.getString(startDateIndex);
                int checkbox1Status = cursor.getInt(checkbox1Index) == 1 ? 1 : 0;
                int checkbox2Status = cursor.getInt(checkbox2Index) == 1 ? 1 : 0;
                int checkbox3Status = cursor.getInt(checkbox3Index) == 1 ? 1 : 0;
                int dayCompleted = cursor.getInt(daysCompleted);

                Habit habit = new Habit(habitId, habitName, difficulty, frequency, startDate);
                habit.setCheckbox1Status(checkbox1Status);
                habit.setCheckbox2Status(checkbox2Status);
                habit.setCheckbox3Status(checkbox3Status);
                habit.setDaysCompleted(dayCompleted);
                habitList.add(habit);
            }
        }

        // Notificar al adaptador que los datos han cambiado
        customAdapterStatistics.notifyDataSetChanged();

        // Mostrar u ocultar el TextView dependiendo de si la lista de hábitos está vacía o no
        if (habitList.isEmpty()) {
            noHabitsTextView.setVisibility(View.VISIBLE);
        } else {
            noHabitsTextView.setVisibility(View.GONE);
        }

        // Cerrar el cursor para liberar recursos
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * Abre la actividad principal.
     */
    private void openMainActivity() {
        Intent intent = new Intent(StatisticsActivity.this, MainActivity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    /**
     * Abre la actividad para agregar un hábito nuevo.
     */
    private void openAddHabitActivity() {
        Intent intent = new Intent(StatisticsActivity.this, AddHabitActivity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    /**
     * Muestra un mensaje indicando que ya se encuentra en la actividad de estadísticas.
     */
    private void openStatisticsActivity() {
        // No hacer nada ya que ya estamos en la actividad de estadísticas
        Toast.makeText(this, "Ya estás en la actividad de estadísticas", Toast.LENGTH_SHORT).show();
    }

    /**
     * Método llamado cuando se hace clic en un hábito en la lista.
     * No se permite la edición desde la actividad de estadísticas.
     *
     * @param position Posición del hábito en la lista.
     */
    @Override
    public void onHabitClick(int position) {
        // No hacer nada ya que no se permite la edición desde la actividad de estadísticas
    }
}