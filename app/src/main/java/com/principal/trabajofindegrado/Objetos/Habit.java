package com.principal.trabajofindegrado.Objetos;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Clase que representa un hábito.
 *
 * @author Jose y Guillermo
 * @version 1.0
 */
public class Habit {
    private int id; // Identificador único del hábito en la base de datos
    private String name; // Nombre del hábito
    private String difficulty; // Dificultad del hábito
    private int frequency; // Frecuencia del hábito
    private String startDate; // Fecha de inicio del hábito
    private int checkbox1Status; // Estado del primer checkbox
    private int checkbox2Status; // Estado del segundo checkbox
    private int checkbox3Status; // Estado del tercer checkbox
    private int daysCompleted; // Dias completados

    /**
     * Constructor de la clase Habit.
     *
     * @param id         Identificador único del hábito en la base de datos
     * @param name       Nombre del hábito
     * @param difficulty Dificultad del hábito
     * @param frequency  Frecuencia del hábito
     * @param startDate  Fecha de inicio del hábito
     */
    public Habit(int id, String name, String difficulty, int frequency, String startDate) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.frequency = frequency;
        this.startDate = startDate;
        // Establecer los valores predeterminados de los checkboxes y dias completados
        this.checkbox1Status = 0;
        this.checkbox2Status = 0;
        this.checkbox3Status = 0;
        this.daysCompleted = 0;
    }

    /**
     * Método para obtener estadísticas sobre el hábito.
     * Este método calcula la cantidad total de veces que se ha marcado cada checkbox.
     *
     * @return Una cadena con las estadísticas de los checkboxes del hábito.
     */
    public String getCheckboxStatistics() {
        // Contador para los checkbox marcados
        int checkedCount = 0;

        // Incrementar el contador si los checkboxes están marcados
        checkedCount += (checkbox1Status == 1) ? 1 : 0;
        checkedCount += (checkbox2Status == 1) ? 1 : 0;
        checkedCount += (checkbox3Status == 1) ? 1 : 0;

        // Construir el mensaje de estadísticas
        String message;
        switch (checkedCount) {
            case 0:
                message = "Hábito no realizado";
                break;
            case 1:
                message = "Hábito realizado una vez";
                break;
            case 2:
                message = "Hábito realizado dos veces";
                break;
            case 3:
                message = "Hábito realizado tres veces";
                break;
            default:
                message = "Estadísticas no disponibles";
                break;
        }

        return message;
    }

    /**
     * Método para calcular el número de días transcurridos desde la fecha de inicio del hábito.
     *
     * @return El número de días transcurridos desde la fecha de inicio del hábito.
     */
    public long calculateDaysSinceStartDate() {
        // Obtener la fecha de inicio del hábito y la fecha actual
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate;
        Date currentDate = new Date();

        try {
            startDate = dateFormat.parse(this.startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Manejo de error en caso de formato de fecha incorrecto
        }

        // Calcular la diferencia en milisegundos entre la fecha actual y la fecha de inicio
        assert startDate != null;
        long differenceMillis = currentDate.getTime() - startDate.getTime();

        // Convertir la diferencia de milisegundos a días

        return TimeUnit.DAYS.convert(differenceMillis, TimeUnit.MILLISECONDS);
    }

    // Métodos getter para obtener los valores de los atributos
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getCheckbox1Status() {
        return checkbox1Status;
    }

    public int getCheckbox2Status() {
        return checkbox2Status;
    }

    public int getCheckbox3Status() {
        return checkbox3Status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDaysCompleted() {
        return daysCompleted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setCheckbox1Status(int checkbox1Status) {
        this.checkbox1Status = checkbox1Status;
    }

    public void setCheckbox2Status(int checkbox2Status) {
        this.checkbox2Status = checkbox2Status;
    }

    public void setCheckbox3Status(int checkbox3Status) {
        this.checkbox3Status = checkbox3Status;
    }

    public void setDaysCompleted(int daysCompleted) {
        this.daysCompleted = daysCompleted;
    }
}