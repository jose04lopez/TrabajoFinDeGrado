package com.principal.trabajofindegrado.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.principal.trabajofindegrado.Database.MyDatabaseHelper;
import com.principal.trabajofindegrado.Objetos.Habit;
import com.principal.trabajofindegrado.R;

import java.util.ArrayList;

/**
 * Adaptador personalizado para mostrar hábitos en un RecyclerView.
 *
 * @author Jose y Guillermo
 * @version 1.0
 */
public class CustomAdapterStatistics extends RecyclerView.Adapter<CustomAdapterStatistics.ViewHolder> {

    private final Context context;
    private final ArrayList<Habit> habitList;
    private OnHabitClickListener onHabitClickListener;
    private MyDatabaseHelper databaseHelper;

    /**
     * Constructor del adaptador.
     *
     * @param context   Contexto de la aplicación
     * @param habitList Lista de hábitos a mostrar
     */
    public CustomAdapterStatistics(Context context, ArrayList<Habit> habitList) {
        this.context = context;
        this.habitList = habitList;
    }

    /**
     * Interfaz para manejar clics en los elementos del RecyclerView.
     */
    public interface OnHabitClickListener {
        void onHabitClick(int position);
    }

    /**
     * Método para establecer el listener de clic en el adaptador.
     *
     * @param listener Listener de clic en los hábitos
     */
    public void setOnHabitClickListener(OnHabitClickListener listener) {
        this.onHabitClickListener = listener;
    }

    /**
     * Método para establecer el ayudante de la base de datos.
     *
     * @param databaseHelper Ayudante de la base de datos
     */
    public void setDatabaseHelper(MyDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Método llamado cuando se crea un nuevo ViewHolder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de cada elemento del RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_habit_statistics, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Método llamado cuando se actualiza el contenido de un ViewHolder.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habitList.get(position);

        // Establecer nombre del hábito
        holder.txtHabitName.setText(habit.getName());

        // Establecer dificultad
        holder.txtDifficulty.setText(context.getString(R.string.difficulty_label) + habit.getDifficulty());

        // Establecer frecuencia
        holder.txtFrequency.setText(context.getString(R.string.frequency_label) + habit.getFrequency());

        // Calcular y establecer el número de días transcurridos
        long daysSinceStartDate = habit.calculateDaysSinceStartDate();
        holder.txtDaysSinceStartDate.setText("Días desde el inicio del hábito: " + daysSinceStartDate);

        // Obtener y establecer las estadísticas del hábito
        String statistics = habit.getCheckboxStatistics();
        holder.txtStatistics.setText(statistics);

        // Agregar listener de clic al elemento del RecyclerView
        holder.itemView.setOnClickListener(v -> {
            if (onHabitClickListener != null) {
                onHabitClickListener.onHabitClick(position);
            }
        });
    }


    /**
     * Método que devuelve la cantidad de elementos en el RecyclerView.
     */
    @Override
    public int getItemCount() {
        return habitList.size();
    }

    /**
     * Clase ViewHolder que representa cada elemento del RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtHabitName;
        TextView txtDifficulty;
        TextView txtFrequency;
        TextView txtStartDate;
        TextView txtStatistics;
        TextView txtDaysSinceStartDate;
        /**
         * Constructor ViewHolder.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Enlazar los TextView y CheckBoxes del diseño de elemento con las variables de la clase ViewHolder
            txtHabitName = itemView.findViewById(R.id.txtHabitName);
            txtDifficulty = itemView.findViewById(R.id.txtDifficulty);
            txtFrequency = itemView.findViewById(R.id.txtFrequency);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtStatistics = itemView.findViewById(R.id.txtStatistics);
            txtDaysSinceStartDate = itemView.findViewById(R.id.txtDaysSinceStartDate);
        }
    }
}
