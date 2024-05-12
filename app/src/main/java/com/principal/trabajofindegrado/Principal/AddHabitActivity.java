package com.principal.trabajofindegrado.Principal;

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

import com.principal.trabajofindegrado.Database.MyDatabaseHelper;
import com.principal.trabajofindegrado.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Esta clase representa la actividad para agregar un nuevo hábito en la aplicación.
 * Permite al usuario agregar hábitos predefinidos o personalizados a su lista de hábitos.
 * También muestra opciones para navegar a otras actividades dentro de la aplicación.
 *
 * @author Jose y Guillermo
 * @version 1.0
 */
public class AddHabitActivity extends AppCompatActivity {
    MyDatabaseHelper databaseHelper;
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
        setContentView(R.layout.activity_add_habit);

        // Inicializar el ayudante de la base de datos
        databaseHelper = new MyDatabaseHelper(this);

        // Obtener los datos del usuario de la actividad anterior
        username = getIntent().getStringExtra("USERNAME");
        userId = getIntent().getStringExtra("USER_ID");

        // Configurar los botones
        configureButtons();

        // Configurar los layouts de hábitos
        configureHabitLayouts();
    }

    /**
     * Configura los botones de la actividad y asigna las acciones correspondientes a cada botón.
     */
    private void configureButtons() {
        // Obtener referencias a los botones
        ImageButton btnToday = findViewById(R.id.btnToday);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ImageButton btnStatistics = findViewById(R.id.btnStatistics);

        // Configurar el botón "Hoy"
        btnToday.setOnClickListener(v -> goToMainActivity());

        // Configurar el botón "Añadir Hábito"
        btnAdd.setOnClickListener(v -> goToAddHabitActivity());

        // Configurar el botón "Estadísticas"
        btnStatistics.setOnClickListener(v -> goToStatisticsActivity());
    }

    /**
     * Configura los layouts de hábitos para mostrar diálogos de confirmación cuando el usuario selecciona un hábito.
     * Estos layouts representan hábitos predefinidos que el usuario puede elegir.
     */
    private void configureHabitLayouts() {
        // Obtener referencias a los layouts de hábitos
        RelativeLayout habit1 = findViewById(R.id.habithelp1);
        RelativeLayout habit2 = findViewById(R.id.habithelp2);
        RelativeLayout habit3 = findViewById(R.id.habithelp3);
        RelativeLayout habit4 = findViewById(R.id.habithelp4);
        RelativeLayout habit5 = findViewById(R.id.habithelp5);
        RelativeLayout habitCustom = findViewById(R.id.habithelp6);

        // Configurar los clics en los layouts de hábitos
        habit1.setOnClickListener(v -> showConfirmationDialog("Ejercicio matutino"));
        habit2.setOnClickListener(v -> showConfirmationDialog("Meditación"));
        habit3.setOnClickListener(v -> showConfirmationDialog("Lectura"));
        habit4.setOnClickListener(v -> showConfirmationDialog("Beber más agua"));
        habit5.setOnClickListener(v -> showConfirmationDialog("Estiramiento"));
        habitCustom.setOnClickListener(v -> showCustomHabitDialog());
    }

    /**
     * Muestra un diálogo de confirmación cuando el usuario selecciona un hábito predefinido.
     * Si el hábito ya está agregado, muestra un mensaje de advertencia.
     *
     * @param habitName Nombre del hábito seleccionado por el usuario.
     */
    private void showConfirmationDialog(String habitName) {
        // Verificar si el hábito ya está agregado
        if (isHabitAdded(habitName)) {
            Toast.makeText(this, "El hábito ya está agregado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construir y mostrar el diálogo de confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que quieres añadir el hábito '" + habitName + "'?")
                .setPositiveButton("Aceptar", (dialog, id) -> addHabitFromLayout(habitName))
                .setNegativeButton("Cancelar", (dialog, id) -> {});
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Añade un hábito a la base de datos cuando el usuario selecciona un hábito predefinido desde un layout.
     * Obtiene la dificultad y la frecuencia del hábito desde la interfaz de usuario y los agrega a la base de datos.
     *
     * @param habitName Nombre del hábito seleccionado por el usuario.
     */
    private void addHabitFromLayout(String habitName) {
        RelativeLayout selectedHabitLayout;
        TextView difficultyTextView;
        TextView frequencyTextView;

        // Obtener referencias a las vistas del layout de hábito seleccionado
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

        // Obtener la dificultad y frecuencia del hábito y añadirlo a la base de datos
        if (difficultyTextView != null && frequencyTextView != null) {
            String difficulty = difficultyTextView.getText().toString().replace("Dificultad: ", "");
            int frequency = Integer.parseInt(frequencyTextView.getText().toString().replace("Frecuencia: ", ""));
            databaseHelper.addHabit(userId, habitName, difficulty, frequency, getCurrentDate());

        } else {
            Toast.makeText(AddHabitActivity.this, "Error: Hábito no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Mostrar un diálogo para añadir un hábito personalizado.
     */
    private void showCustomHabitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_habit, null);
        builder.setView(dialogView);

        EditText habitNameEditText = dialogView.findViewById(R.id.editText_habit_name);
        Spinner difficultySpinner = dialogView.findViewById(R.id.spinner_difficulty);
        Spinner frequencySpinner = dialogView.findViewById(R.id.spinner_frequency);

        // Configurar el diálogo para el hábito personalizado
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

    /**
     * Obtener la fecha actual en el formato dd-MM-yyyy.
     *
     * @return Fecha actual en formato de cadena de caracteres
     */
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * Verificar si un hábito ya ha sido agregado por el usuario.
     *
     * @param habitName Nombre del hábito
     * @return true si el hábito ya ha sido agregado, false de lo contrario
     */
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

    /**
     * Navegar a la actividad principal (TodayActivity).
     */
    private void goToMainActivity() {
        Intent intent = new Intent(AddHabitActivity.this, MainActivity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    /**
     * Navegar a la actividad de añadir hábito (AddHabitActivity).
     */
    private void goToAddHabitActivity() {
        Toast.makeText(this, "Ya estás en la actividad para añadir habitos", Toast.LENGTH_SHORT).show();
    }

    /**
     * Navegar a la actividad de estadísticas (StatisticsActivity).
     */
    private void goToStatisticsActivity() {
        Intent intent = new Intent(AddHabitActivity.this, StatisticsActivity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }
}