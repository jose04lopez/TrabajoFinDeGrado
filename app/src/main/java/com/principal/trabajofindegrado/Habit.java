package com.principal.trabajofindegrado;

// Clase que representa un hábito
public class Habit {
    private int id; // Identificador único del hábito en la base de datos
    private String name; // Nombre del hábito
    private String difficulty; // Dificultad del hábito
    private int frequency; // Frecuencia del hábito
    private String startDate; // Fecha de inicio del hábito
    private boolean isCompletionStatus; // Estado de completitud del hábito (completado o no)

    // Constructor de la clase Habit
    public Habit(int id, String name, String difficulty, int frequency, String startDate, boolean isCompletionStatus) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.frequency = frequency;
        this.startDate = startDate;
        this.isCompletionStatus = isCompletionStatus;
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

    public boolean isCompletionStatus() {
        return isCompletionStatus;
    }

    // Métodos setter para establecer los valores de los atributos
    public void setId(int id) {
        this.id = id;
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

    public void setCompletionStatus(boolean completionStatus) {
        isCompletionStatus = completionStatus;
    }
}
