package com.principal.trabajofindegrado.Principal;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.principal.trabajofindegrado.Database.MyDatabaseHelper;
import com.principal.trabajofindegrado.R;

import java.util.Calendar;

/**
 * Activity para gestionar la configuración de la aplicación.
 * Permite al usuario cambiar la contraseña, activar/desactivar las notificaciones, cerrar sesión
 * y eliminar la cuenta de usuario.
 *
 *  @author Jose y Guillermo
 *  @version 1.0
 */
public class SettingsActivity extends AppCompatActivity {

    // Variables miembro
    private SharedPreferences sharedPreferences;
    private PendingIntent pendingIntent;
    private String userId, username;

    /**
     * Método llamado cuando la actividad se está iniciando.
     *
     * @param savedInstanceState Los datos de la instancia previamente guardada.
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Obtener la referencia de SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Obtener el estado del interruptor de notificaciones de SharedPreferences
        boolean isNotificationEnabled = sharedPreferences.getBoolean("notification_enabled", false);

        // Obtener el nombre de usuario y el ID de usuario de la intención anterior
        username = getIntent().getStringExtra("USERNAME");
        userId = getIntent().getStringExtra("USER_ID");

        TextView userInfoTextView = findViewById(R.id.textView_user_info);
        RelativeLayout logoutTextView = findViewById(R.id.rl_logout);
        RelativeLayout deleteAccountTextView = findViewById(R.id.rl_delete_account);

        userInfoTextView.setText("Nombre de usuario: " + username);

        // Agregar un OnClickListener al TextView de Cambiar Contraseña
        RelativeLayout changePasswordLayout = findViewById(R.id.rl_change_password);
        changePasswordLayout.setOnClickListener(v -> {
            // Crear y mostrar un cuadro de diálogo de alerta
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);
            alertDialogBuilder.setTitle("Cambiar Contraseña");

            // Inflar el layout personalizado para el cuadro de diálogo
            View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
            alertDialogBuilder.setView(view);

            // Obtener referencias a los elementos del layout del cuadro de diálogo
            EditText newPasswordEditText = view.findViewById(R.id.editText_new_password);
            EditText repeatPasswordEditText = view.findViewById(R.id.editText_repeat_password);

            // Agregar botones de guardado y cancelación al cuadro de diálogo
            // Configuración del TextView de Cambiar Contraseña
            alertDialogBuilder.setPositiveButton("Guardar", (dialog, which) -> {
                // Implementación del cuadro de diálogo de cambio de contraseña
                String newPassword = newPasswordEditText.getText().toString();
                String repeatPassword = repeatPasswordEditText.getText().toString();

                // Verificar si las contraseñas coinciden
                if (newPassword.equals(repeatPassword)) {
                    // Crear una instancia de MyDatabaseHelper
                    MyDatabaseHelper dbHelper = new MyDatabaseHelper(SettingsActivity.this);

                    // Obtener la contraseña actual del usuario
                    String currentPassword = dbHelper.getPasswordByUsername(username);

                    // Verificar si la nueva contraseña es diferente de la contraseña actual
                    if (currentPassword != null && currentPassword.equals(newPassword)) {
                        // Mostrar un mensaje de error si la nueva contraseña es igual a la contraseña actual
                        Toast.makeText(SettingsActivity.this, "La nueva contraseña debe ser diferente de la contraseña actual", Toast.LENGTH_SHORT).show();
                    } else {
                        // Cambiar la contraseña del usuario
                        dbHelper.changePassword(username, newPassword);

                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Esto evita que el usuario pueda volver atrás con el botón de atrás
                    }
                } else {
                    // Mostrar un mensaje de error si las contraseñas no coinciden
                    Toast.makeText(SettingsActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            });

            alertDialogBuilder.setNegativeButton("Cancelar", (dialog, which) -> {
                // No hacer nada, simplemente cerrar el cuadro de diálogo
                dialog.dismiss();
            });

            // Mostrar el cuadro de diálogo
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        // Configuración del TextView de Cerrar Sesión
        logoutTextView.setOnClickListener(v -> {
            // Implementación del cierre de sesión
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            // Limpiar la pila de actividades y abrir la LoginActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Configuración del TextView de Eliminar Cuenta
        deleteAccountTextView.setOnClickListener(v -> {
            // Implementación del cuadro de diálogo de eliminación de cuenta
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Eliminar cuenta");
            builder.setMessage("¿Estás seguro de que quieres eliminar la cuenta?");

            // Agregar botones de confirmación y cancelación
            builder.setPositiveButton("Aceptar", (dialog, which) -> {
                // Obtener el nombre de usuario actual
                String username = getIntent().getStringExtra("USERNAME");
                // Eliminar la cuenta de usuario de la base de datos
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(SettingsActivity.this);
                dbHelper.deleteUser(username);

                // Redirigir a la pantalla de inicio de sesión
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Esto evita que el usuario pueda volver atrás con el botón de atrás
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                // No hacer nada, simplemente cerrar el cuadro de diálogo
                dialog.dismiss();
            });

            // Mostrar el cuadro de diálogo
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        // Configuración del interruptor de notificaciones
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchNotifications = findViewById(R.id.switch_notifications);
        switchNotifications.setChecked(isNotificationEnabled);

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                scheduleNotification();
            } else {
                cancelNotification();
            }

            // Guardar el estado del interruptor de notificaciones en SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("notification_enabled", isChecked);
            editor.apply();
        });

        // Configuración de los botones de ayuda, hoy, agregar y estadísticas
        RelativeLayout rlHelp = findViewById(R.id.rl_help);
        ImageButton btnToday = findViewById(R.id.btnToday);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ImageButton btnStatistics = findViewById(R.id.btnStatistics);

        rlHelp.setOnClickListener(v -> {
            // Implementación para redirigir a la actividad SettingsActivity
            Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);

            intent.putExtra("USERNAME", username);
            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });

        btnToday.setOnClickListener(v -> {
            // Implementación para redirigir a la actividad MainActivity
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);

            intent.putExtra("USERNAME", username);
            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });

        btnAdd.setOnClickListener(v -> {
            // Implementación para redirigir a la actividad AddHabitActivity
            Intent intent = new Intent(SettingsActivity.this, AddHabitActivity.class);

            intent.putExtra("USERNAME", username);
            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });

        btnStatistics.setOnClickListener(v -> {
            // Implementación para redirigir a la actividad StatisticsActivity
            Intent intent = new Intent(SettingsActivity.this, StatisticsActivity.class);

            intent.putExtra("USERNAME", username);
            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });
    }

    /**
     * Programa una notificación diaria para el usuario.
     */
    private void scheduleNotification() {
        // Implementación de la programación de la notificación
        Intent notificationIntent = new Intent(this, SettingsActivity.class);

        // Agrega el flag FLAG_IMMUTABLE o FLAG_MUTABLE dependiendo de la versión de Android
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, flags);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        // Crear y mostrar la notificación
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "canal_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Notificación de HabitNow")
                .setContentText("Tus noficicaciones diarias han sido activadas")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    /**
     * Cancela la notificación programada.
     */
    private void cancelNotification() {
        // Implementación de la cancelación de la notificación
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    /**
     * Crea el canal de notificación requerido para versiones de Android 8.0 y posteriores.
     */
    private void createNotificationChannel() {
        // Implementación de la creación del canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Nombre del canal";
            String description = "Descripción del canal";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("canal_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    /**
     * La clase HelpActivity muestra opciones para que el usuario navegue a diferentes funcionalidades,
     * como ver los hábitos de hoy, agregar nuevos hábitos y verificar estadísticas.
     *
     * @author Jose y Guillermo
     * @version 1.0
     */
    public static class HelpActivity extends AppCompatActivity {

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
            setContentView(R.layout.activity_help);

            // Recupera el nombre de usuario y el ID de usuario pasados desde la actividad anterior
            username = getIntent().getStringExtra("USERNAME");
            userId = getIntent().getStringExtra("USER_ID");

            // Inicializa los botones para navegar a diferentes funcionalidades
            ImageButton btnToday = findViewById(R.id.btnToday);
            ImageButton btnAdd = findViewById(R.id.btnAdd);
            ImageButton btnStatistics = findViewById(R.id.btnStatistics);

            // Establece OnClickListener para navegar a MainActivity y ver los hábitos de hoy
            btnToday.setOnClickListener(v -> {
                Intent intent = new Intent(HelpActivity.this, MainActivity.class);

                // Pasa el nombre de usuario y el ID de usuario a MainActivity
                intent.putExtra("USERNAME", username);
                intent.putExtra("USER_ID", userId);

                startActivity(intent);
            });

            // Establece OnClickListener para navegar a AddHabitActivity y agregar nuevos hábitos
            btnAdd.setOnClickListener(v -> {
                Intent intent = new Intent(HelpActivity.this, AddHabitActivity.class);

                // Pasa el nombre de usuario y el ID de usuario a AddHabitActivity
                intent.putExtra("USERNAME", username);
                intent.putExtra("USER_ID", userId);

                startActivity(intent);
            });

            // Establece OnClickListener para navegar a StatisticsActivity y verificar estadísticas
            btnStatistics.setOnClickListener(v -> {
                Intent intent = new Intent(HelpActivity.this, StatisticsActivity.class);

                // Pasa el nombre de usuario y el ID de usuario a StatisticsActivity
                intent.putExtra("USERNAME", username);
                intent.putExtra("USER_ID", userId);

                startActivity(intent);
            });
        }
    }
}
