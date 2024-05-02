package com.principal.trabajofindegrado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch switchNotifications = findViewById(R.id.switch_notifications);

        // Inicializa el RelativeLayout correspondiente al botón de ayuda
        RelativeLayout rlHelp = findViewById(R.id.rl_help);

        // Configurar eventos para las imágenes del menú inferior
        ImageButton btnToday = findViewById(R.id.btnToday);
        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ImageButton btnStatistics = findViewById(R.id.btnStatistics);

        // Configurar el evento de clic para el botón de ayuda
        rlHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la nueva pantalla (Activity de ayuda)
                Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        // Configurar eventos de clic para los botones del menú inferior
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar MainActivity
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                // Finalizar SettingsActivity si deseas que se cierre al abrir MainActivity
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar aquí la lógica para el botón de "Agregar"
            }
        });

        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar aquí la lógica para el botón de "Estadísticas"
            }
        });

        // Configurar el listener para el Switch de notificaciones
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Aquí puedes agregar la lógica para manejar el cambio de estado del Switch
                if (isChecked) {
                    // Si el Switch está activado (isChecked == true), puedes realizar alguna acción
                    // Por ejemplo, mostrar un mensaje o activar las notificaciones
                } else {
                    // Si el Switch está desactivado (isChecked == false), puedes realizar alguna otra acción
                    // Por ejemplo, ocultar un elemento o desactivar las notificaciones
                }
            }
        });
    }

    // Definir HelpActivity como una clase interna de SettingsActivity
    public static class HelpActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_help);

            ImageButton btnToday = findViewById(R.id.btnToday);
            ImageButton btnAdd = findViewById(R.id.btnAdd);
            ImageButton btnStatistics = findViewById(R.id.btnStatistics);

            // Configurar eventos de clic para los botones del menú inferior
            btnToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Iniciar MainActivity
                    Intent intent = new Intent(HelpActivity.this, MainActivity.class);
                    startActivity(intent);
                    // Finalizar SettingsActivity si deseas que se cierre al abrir MainActivity
                    finish();
                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Agregar aquí la lógica para el botón de "Agregar"
                }
            });

            btnStatistics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Agregar aquí la lógica para el botón de "Estadísticas"
                }
            });
        }
    }
}
