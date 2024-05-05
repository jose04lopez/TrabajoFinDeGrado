package com.principal.trabajofindegrado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText, dateOfBirthEditText, emailEditText, phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar vistas
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        dateOfBirthEditText = findViewById(R.id.editTextDate);
        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        phoneEditText = findViewById(R.id.editTextPhone);

        // Configurar el click listener para el botón de registro
        Button signupButton = findViewById(R.id.loginButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores ingresados por el usuario
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String dateOfBirth = dateOfBirthEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                // Guardar los datos del usuario en la base de datos
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(RegisterActivity.this);
                dbHelper.addUser(username, password, dateOfBirth, email, phone);

            }
        });

        // Configurar el click listener para el texto "Iniciar Sesión"
        TextView signinText = findViewById(R.id.signupText);
        signinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes implementar la lógica para dirigir al usuario a la pantalla de inicio de sesión
                // Por ejemplo, iniciar una nueva actividad
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
