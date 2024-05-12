package com.principal.trabajofindegrado.Principal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.principal.trabajofindegrado.Database.MyDatabaseHelper;
import com.principal.trabajofindegrado.R;

/**
 * Esta actividad permite a los usuarios registrarse en la aplicación.
 *
 *  @author Jose y Guillermo
 *  @version 1.0
 */
public class RegisterActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText, dateOfBirthEditText, emailEditText, phoneEditText;

    /**
     * Método llamado cuando se crea la actividad. Configura la interfaz de usuario y
     * establece los listeners de clic para los botones de navegación.
     *
     * @param savedInstanceState Objeto Bundle que contiene el estado previamente guardado de la actividad.
     */
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
        signupButton.setOnClickListener(v -> {
            // Obtener los valores ingresados por el usuario
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String dateOfBirth = dateOfBirthEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone = phoneEditText.getText().toString();

            // Guardar los datos del usuario en la base de datos
            MyDatabaseHelper dbHelper = new MyDatabaseHelper(RegisterActivity.this);
            dbHelper.addUser(username, password, dateOfBirth, email, phone);

        });

        // Configurar el click listener para el texto "Iniciar Sesión"
        TextView signinText = findViewById(R.id.signupText);
        signinText.setOnClickListener(v -> {
            // Aquí puedes implementar la lógica para dirigir al usuario a la pantalla de inicio de sesión
            // Por ejemplo, iniciar una nueva actividad
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
