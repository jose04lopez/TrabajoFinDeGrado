package com.principal.trabajofindegrado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Habit> habitList;
    private OnHabitClickListener onHabitClickListener;

    // Constructor del adaptador
    public CustomAdapter(Context context, ArrayList<Habit> habitList) {
        this.context = context;
        this.habitList = habitList;
    }

    // Interfaz para manejar clics en los elementos del RecyclerView
    public interface OnHabitClickListener {
        void onHabitClick(int position);
    }

    // Método para establecer el listener de clic en el adaptador
    public void setOnHabitClickListener(OnHabitClickListener listener) {
        this.onHabitClickListener = listener;
    }

    // Método llamado cuando se crea un nuevo ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de cada elemento del RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_habit, parent, false);
        return new ViewHolder(view);
    }

    // Método llamado cuando se actualiza el contenido de un ViewHolder
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Habit habit = habitList.get(position);

        // Set habit name
        holder.txtHabitName.setText(habit.getName());

        // Set difficulty with resource string
        holder.txtDifficulty.setText(context.getString(R.string.difficulty_label) + habit.getDifficulty());

        // Set frequency with resource string
        holder.txtFrequency.setText(context.getString(R.string.frequency_label) + habit.getFrequency());

        // Set start date with resource string
        holder.txtStartDate.setText(context.getString(R.string.start_date_label) + habit.getStartDate());

        // Add click listener to the RecyclerView item
        holder.itemView.setOnClickListener(v -> {
            if (onHabitClickListener != null) {
                onHabitClickListener.onHabitClick(position);
            }
        });
    }


    // Método que devuelve la cantidad de elementos en el RecyclerView
    @Override
    public int getItemCount() {
        return habitList.size();
    }

    // Clase ViewHolder que representa cada elemento del RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtHabitName;
        TextView txtDifficulty;
        TextView txtFrequency;
        TextView txtStartDate;

        // Constructor ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Enlazar los TextView del diseño de elemento con las variables de la clase ViewHolder
            txtHabitName = itemView.findViewById(R.id.txtHabitName);
            txtDifficulty = itemView.findViewById(R.id.txtDifficulty);
            txtFrequency = itemView.findViewById(R.id.txtFrequency);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
        }
    }
}
