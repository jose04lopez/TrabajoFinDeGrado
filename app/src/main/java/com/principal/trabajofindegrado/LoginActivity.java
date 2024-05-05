package com.principal.trabajofindegrado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private MyDatabaseHelper dbHelper; // Instancia de la clase MyDatabaseHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas y base de datos helper
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        dbHelper = new MyDatabaseHelper(this); // Inicializar el helper de la base de datos

        // Configurar el click listener para el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores de usuario y contraseña ingresados por el usuario
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Verificar si los campos de usuario y contraseña no están vacíos
                if (!username.isEmpty() && !password.isEmpty()) {
                    // Verificar las credenciales en la base de datos
                    MyDatabaseHelper dbHelper = new MyDatabaseHelper(LoginActivity.this);
                    ValidationResult result = dbHelper.validateCredentials(username, password);

                    if (result.isValid()) {
                        // Si las credenciales son válidas, redirigir a MainActivity
                        // After a successful login
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        intent.putExtra("USERNAME", username);
                        intent.putExtra("USER_ID", result.getUserId());

                        startActivity(intent);
                    } else {
                        // Si las credenciales no son válidas, mostrar un mensaje de error
                        Toast.makeText(LoginActivity.this, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si los campos están vacíos, mostrar un mensaje de error
                    Toast.makeText(LoginActivity.this, "Por favor, ingresa tu usuario y contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar el click listener para el texto "Registrarse"
        TextView signupText = findViewById(R.id.signupText);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes implementar la lógica para dirigir al usuario a la pantalla de registro
                // Por ejemplo, iniciar una nueva actividad
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
