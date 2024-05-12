package com.principal.trabajofindegrado.Principal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.principal.trabajofindegrado.Database.MyDatabaseHelper;
import com.principal.trabajofindegrado.R;
import com.principal.trabajofindegrado.Objetos.ValidationResult;

/**
 * Actividad para el inicio de sesión de usuario.
 *
 * @author Jose y Guillermo
 * @version 1.0
 */
public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;

    /**
     * Método llamado cuando se crea la actividad.
     *
     * @param savedInstanceState La instancia previamente guardada del estado de esta actividad, si existe.
     *                           Si no es nulo, este objeto contiene el estado de la actividad antes de ser cerrada,
     *                           que puede recuperarse fácilmente para restablecer el estado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas y base de datos helper
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        TextView signupText = findViewById(R.id.signupText);

        // Configurar el click listener para el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Método llamado cuando se hace clic en el botón de inicio de sesión.
             *
             * @param v La vista que ha sido clicada (en este caso, el botón de inicio de sesión).
             */
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
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        intent.putExtra("USERNAME", username);
                        intent.putExtra("USER_ID", result.getUserId());

                        startActivity(intent);
                        finish();
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
        signupText.setOnClickListener(new View.OnClickListener() {
            /**
             * Método llamado cuando se hace clic en el texto de "Registrarse".
             *
             * @param v La vista que ha sido clicada (en este caso, el texto de "Registrarse").
             */
            @Override
            public void onClick(View v) {
                // Dirigir al usuario a la pantalla de registro
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
