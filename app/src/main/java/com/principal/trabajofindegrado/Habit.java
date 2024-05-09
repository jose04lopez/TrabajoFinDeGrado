package com.principal.trabajofindegrado;

// Clase que representa un hábito
public class Habit {
    private int id; // Identificador único del hábito en la base de datos
    private String name; // Nombre del hábito
    private String difficulty; // Dificultad del hábito
    private int frequency; // Frecuencia del hábito
    private String startDate; // Fecha de inicio del hábito
    private int checkbox1Status; // Estado del primer checkbox
    private int checkbox2Status; // Estado del segundo checkbox
    private int checkbox3Status; // Estado del tercer checkbox

    // Constructor de la clase Habit
    public Habit(int id, String name, String difficulty, int frequency, String startDate) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.frequency = frequency;
        this.startDate = startDate;
        // Establecer los valores predeterminados de los checkboxs
        this.checkbox1Status = 0;
        this.checkbox2Status = 0;
        this.checkbox3Status = 0;
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

    public void setCheckbox1Status(int checkbox1Status) {
        this.checkbox1Status = checkbox1Status;
    }

    public void setCheckbox2Status(int checkbox2Status) {
        this.checkbox2Status = checkbox2Status;
    }

    public void setCheckbox3Status(int checkbox3Status) {
        this.checkbox3Status = checkbox3Status;
    }
}
