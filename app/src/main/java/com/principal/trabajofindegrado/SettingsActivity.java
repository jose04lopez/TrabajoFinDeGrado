// SettingsActivity.java

package com.principal.trabajofindegrado;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private boolean isNotificationEnabled = false;
    private PendingIntent pendingIntent;
    private String userId, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Obtener la referencia de SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Obtener el estado del interruptor de notificaciones de SharedPreferences
        isNotificationEnabled = sharedPreferences.getBoolean("notification_enabled", false);

        username = getIntent().getStringExtra("USERNAME");
        userId = getIntent().getStringExtra("USER_ID");

        TextView userInfoTextView = findViewById(R.id.textView_user_info);
        TextView logoutTextView = findViewById(R.id.textView_logout);
        TextView deleteAccountTextView = findViewById(R.id.textView_delete_account);

        userInfoTextView.setText("Nombre de usuario: " + username);

        // Agregar un OnClickListener al TextView de Cambiar Contraseña
        RelativeLayout changePasswordLayout = findViewById(R.id.rl_change_password);
        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                alertDialogBuilder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                    }

                });

                alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada, simplemente cerrar el cuadro de diálogo
                        dialog.dismiss();
                    }
                });

                // Mostrar el cuadro de diálogo
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        // Agregar un OnClickListener al TextView
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar la LoginActivity
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                // Limpiar la pila de actividades y abrir la LoginActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Agregar un OnClickListener al TextView
        deleteAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un cuadro de diálogo de confirmación
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Eliminar cuenta");
                builder.setMessage("¿Estás seguro de que quieres eliminar la cuenta?");

                // Agregar botones de confirmación y cancelación
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Obtener el nombre de usuario actual
                        String username = getIntent().getStringExtra("USERNAME");
                        // Eliminar la cuenta de usuario de la base de datos
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(SettingsActivity.this);
                        dbHelper.deleteUser(username);

                        // Redirigir a la pantalla de inicio de sesión
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Esto evita que el usuario pueda volver atrás con el botón de atrás
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada, simplemente cerrar el cuadro de diálogo
                        dialog.dismiss();
                    }
                });

                // Mostrar el cuadro de diálogo
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "canal_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Estado de las notificaciones")
                .setContentText("Activadas")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Switch switchNotifications = findViewById(R.id.switch_notifications);
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

        RelativeLayout rlHelp = findViewById(R.id.rl_help);
        ImageButton btnToday = findViewById(R.id.btnToday);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ImageButton btnStatistics = findViewById(R.id.btnStatistics);

        rlHelp.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);

            intent.putExtra("USERNAME", username);
            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });

        btnToday.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);

            intent.putExtra("USERNAME", username);
            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AddHabitActivity.class);

            intent.putExtra("USERNAME", username);
            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });

        btnStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, StatisticsActivity.class);

            intent.putExtra("USERNAME", username);
            intent.putExtra("USER_ID", userId);

            startActivity(intent);
        });
    }

    private void scheduleNotification() {
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
                .setContentTitle("Título de la notificación")
                .setContentText("Tu mensaje de notificación")
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

    private void cancelNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    // Método para crear el canal de notificación (solo para versiones de Android posteriores a 8.0)
    private void createNotificationChannel() {
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


public static class HelpActivity extends AppCompatActivity {

        private String userId, username;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_help);

            username = getIntent().getStringExtra("USERNAME");
            userId = getIntent().getStringExtra("USER_ID");

            ImageButton btnToday = findViewById(R.id.btnToday);
            ImageButton btnAdd = findViewById(R.id.btnAdd);
            ImageButton btnStatistics = findViewById(R.id.btnStatistics);

            btnToday.setOnClickListener(v -> {
                Intent intent = new Intent(HelpActivity.this, MainActivity.class);

                intent.putExtra("USERNAME", username);
                intent.putExtra("USER_ID", userId);

                startActivity(intent);
            });

            btnAdd.setOnClickListener(v -> {
                Intent intent = new Intent(HelpActivity.this, AddHabitActivity.class);

                intent.putExtra("USERNAME", username);
                intent.putExtra("USER_ID", userId);

                startActivity(intent);
            });

            btnStatistics.setOnClickListener(v -> {
                Intent intent = new Intent(HelpActivity.this, StatisticsActivity.class);

                intent.putExtra("USERNAME", username);
                intent.putExtra("USER_ID", userId);

                startActivity(intent);
            });
        }
    }
}
