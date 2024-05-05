package com.principal.trabajofindegrado;

public class Habit {
    private int id;
    private String name;
    private String difficulty;
    private int frequency;
    private String startDate;

    public Habit(int id, String name, String difficulty, int frequency, String startDate) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.frequency = frequency;
        this.startDate = startDate;
    }

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
}

