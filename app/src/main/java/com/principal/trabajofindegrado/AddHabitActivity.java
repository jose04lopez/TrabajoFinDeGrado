package com.principal.trabajofindegrado;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddHabitActivity extends AppCompatActivity {

    private EditText editTextHabitName;
    private Spinner spinnerTime;
    private Button buttonAddHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        editTextHabitName = findViewById(R.id.editTextHabitName);
        spinnerTime = findViewById(R.id.spinnerTime);
        buttonAddHabit = findViewById(R.id.buttonAddHabit);

        buttonAddHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí debes implementar la lógica para añadir el hábito a tu base de datos o almacenamiento.
                // Por ejemplo, puedes obtener el nombre del hábito y el tiempo seleccionado y guardarlo en tu base de datos.
                String habitName = editTextHabitName.getText().toString();
                String time = spinnerTime.getSelectedItem().toString();

                // Luego de guardar el hábito, puedes mostrar un mensaje de éxito.
                Toast.makeText(AddHabitActivity.this, "Hábito añadido: " + habitName + " - " + time, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

