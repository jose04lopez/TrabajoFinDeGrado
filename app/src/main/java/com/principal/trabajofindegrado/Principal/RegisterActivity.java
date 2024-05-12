package com.principal.trabajofindegrado.Principal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.principal.trabajofindegrado.Database.MyDatabaseHelper;
import com.principal.trabajofindegrado.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Esta actividad permite a los usuarios registrarse en la aplicación.
 * Permite a los usuarios crear una cuenta proporcionando información como nombre de usuario, contraseña,
 * fecha de nacimiento, correo electrónico y número de teléfono. Se realizan validaciones en los campos
 * introducidos para garantizar que los datos sean correctos antes de registrar al usuario en la base de datos.
 * Si el usuario ya existe, se mostrará un mensaje de error y no se realizará el registro.
 *
 * @author Jose y Guillermo
 * @version 1.0
 */
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
        signupButton.setOnClickListener(v -> {
            // Obtener los valores ingresados por el usuario
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String dateOfBirth = dateOfBirthEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone = phoneEditText.getText().toString();

            // Validar datos
            if (username.isEmpty() || password.isEmpty() || dateOfBirth.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(email)) {
                emailEditText.setError("Correo electrónico no válido");
                return;
            }

            if (!isValidDateOfBirth(dateOfBirth)) {
                dateOfBirthEditText.setError("Formato de fecha incorrecto. Utilice dd-mm-yyyy");
                return;
            }

            if (!isValidPhoneNumber(phone)) {
                phoneEditText.setError("Número de teléfono no válido");
                return;
            }

            // Verificar si el usuario ya existe antes de agregarlo a la base de datos
            MyDatabaseHelper dbHelper = new MyDatabaseHelper(RegisterActivity.this);
            if (dbHelper.isUserExists(username)) {
                Toast.makeText(RegisterActivity.this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                return; // No realizar más acciones si el usuario ya existe
            }

            // Si el usuario no existe y los datos son válidos, agregarlo a la base de datos
            dbHelper.addUser(username, password, dateOfBirth, email, phone);

            // Redirigir al usuario a LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);

            // Limpiar campos después del registro exitoso
            usernameEditText.getText().clear();
            passwordEditText.getText().clear();
            dateOfBirthEditText.getText().clear();
            emailEditText.getText().clear();
            phoneEditText.getText().clear();
        });

        // Configurar el click listener para el texto "Iniciar Sesión"
        TextView signinText = findViewById(R.id.signupText);
        signinText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Valida si la cadena proporcionada es un correo electrónico válido.
     *
     * @param email La cadena a validar como correo electrónico.
     * @return true si la cadena es un correo electrónico válido, de lo contrario false.
     */
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Valida si la cadena proporcionada sigue el formato de fecha "dd-MM-yyyy".
     *
     * @param dateOfBirth La cadena a validar como fecha de nacimiento.
     * @return true si la cadena sigue el formato de fecha válido, de lo contrario false.
     */
    @SuppressLint("SimpleDateFormat")
    private boolean isValidDateOfBirth(String dateOfBirth) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateOfBirth);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Valida si la cadena proporcionada consiste solo en dígitos.
     *
     * @param phone La cadena a validar como número de teléfono.
     * @return true si la cadena contiene solo dígitos, de lo contrario false.
     */
    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{9}");
    }
}
