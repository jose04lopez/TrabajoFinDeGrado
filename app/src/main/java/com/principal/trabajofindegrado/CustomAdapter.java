package com.principal.trabajofindegrado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Habit> habitList;

    public CustomAdapter(Context context, ArrayList<Habit> habitList) {
        this.context = context;
        this.habitList = habitList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_habit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habitList.get(position);
        holder.txtHabitName.setText(habit.getName());
        holder.txtDifficulty.setText("Dificultad: " + habit.getDifficulty());
        holder.txtFrequency.setText("Frecuencia: " + habit.getFrequency());
        holder.txtStartDate.setText("Inicio: " + habit.getStartDate());
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtHabitName;
        TextView txtDifficulty;
        TextView txtFrequency;
        TextView txtStartDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHabitName = itemView.findViewById(R.id.txtHabitName);
            txtDifficulty = itemView.findViewById(R.id.txtDifficulty);
            txtFrequency = itemView.findViewById(R.id.txtFrequency);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
        }
    }
}

