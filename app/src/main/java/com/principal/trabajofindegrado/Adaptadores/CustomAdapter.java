package com.principal.trabajofindegrado.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

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
    public CustomAdapter(Context context, ArrayList<Habit> habitList) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_habit, parent, false);
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

        // Establecer dificultad con string de recursos
        holder.txtDifficulty.setText(context.getString(R.string.difficulty_label) + habit.getDifficulty());

        // Establecer frecuencia con string de recursos
        holder.txtFrequency.setText(context.getString(R.string.frequency_label) + habit.getFrequency());

        // Establecer fecha de inicio con string de recursos
        holder.txtStartDate.setText(context.getString(R.string.start_date_label) + habit.getStartDate());

        // Establecer visibilidad de los checkboxes basada en la frecuencia
        switch (habit.getFrequency()) {
            case 1:
                holder.checkBox1.setVisibility(View.VISIBLE);
                holder.checkBox2.setVisibility(View.GONE);
                holder.checkBox3.setVisibility(View.GONE);
                holder.checkBox1.setChecked(habit.getCheckbox1Status() == 1);
                // Agregar lógica para verificar y mostrar Toast si solo el CheckBox 1 está marcado
                holder.checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    habit.setCheckbox1Status(isChecked ? 1 : 0);
                    databaseHelper.updateCheckboxStatus(habit.getId(), 1, isChecked ? 1 : 0);
                    checkAndShowToastIfOnlyCheckBox1Checked(holder.checkBox1, habit.getId());
                });
                break;
            case 2:
                holder.checkBox1.setVisibility(View.VISIBLE);
                holder.checkBox2.setVisibility(View.VISIBLE);
                holder.checkBox3.setVisibility(View.GONE);
                holder.checkBox1.setChecked(habit.getCheckbox1Status() == 1);
                holder.checkBox2.setChecked(habit.getCheckbox2Status() == 1);
                // Agregar lógica para verificar y mostrar Toast si ambos CheckBox 1 y 2 están marcados
                holder.checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    habit.setCheckbox1Status(isChecked ? 1 : 0);
                    databaseHelper.updateCheckboxStatus(habit.getId(), 1, isChecked ? 1 : 0);
                    checkAndShowToastIfBothCheckBox1AndCheckBox2Checked(holder.checkBox1, holder.checkBox2, habit.getId());
                });

                holder.checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    habit.setCheckbox2Status(isChecked ? 1 : 0);
                    databaseHelper.updateCheckboxStatus(habit.getId(), 2, isChecked ? 1 : 0);
                    checkAndShowToastIfAllChecked(holder.checkBox1, holder.checkBox2, holder.checkBox3, habit.getId());
                });
                break;
            case 3:
                holder.checkBox1.setVisibility(View.VISIBLE);
                holder.checkBox2.setVisibility(View.VISIBLE);
                holder.checkBox3.setVisibility(View.VISIBLE);
                holder.checkBox1.setChecked(habit.getCheckbox1Status() == 1);
                holder.checkBox2.setChecked(habit.getCheckbox2Status() == 1);
                holder.checkBox3.setChecked(habit.getCheckbox3Status() == 1);
                // Agregar lógica para mostrar Toast cuando todos los CheckBox están marcados
                holder.checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    habit.setCheckbox1Status(isChecked ? 1 : 0);
                    databaseHelper.updateCheckboxStatus(habit.getId(), 1, isChecked ? 1 : 0);
                    checkAndShowToastIfAllChecked(holder.checkBox1, holder.checkBox2, holder.checkBox3, habit.getId());
                });

                holder.checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    habit.setCheckbox2Status(isChecked ? 1 : 0);
                    databaseHelper.updateCheckboxStatus(habit.getId(), 2, isChecked ? 1 : 0);
                    checkAndShowToastIfAllChecked(holder.checkBox1, holder.checkBox2, holder.checkBox3, habit.getId());
                });

                holder.checkBox3.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    habit.setCheckbox3Status(isChecked ? 1 : 0);
                    databaseHelper.updateCheckboxStatus(habit.getId(), 3, isChecked ? 1 : 0);
                    checkAndShowToastIfAllChecked(holder.checkBox1, holder.checkBox2, holder.checkBox3, habit.getId());
                });
                break;
            default:
                break;
        }

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
        CheckBox checkBox1, checkBox2, checkBox3;

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
            checkBox1 = itemView.findViewById(R.id.checkBox1);
            checkBox2 = itemView.findViewById(R.id.checkBox2);
            checkBox3 = itemView.findViewById(R.id.checkBox3);
        }
    }

    /**
     * Verifica si solo el CheckBox 1 está marcado y muestra un Toast si es así.
     *
     * @param checkBox1 El CheckBox 1 a verificar.
     * @param idHabit El id del habito al que aumentarle el valor.
     */
    private void checkAndShowToastIfOnlyCheckBox1Checked(CheckBox checkBox1, int idHabit) {
        if (checkBox1.isChecked()) {
            // Mostrar Toast cuando solo el CheckBox 1 está marcado
            Toast.makeText(context, "¡Enhorabuena por completar tu hábito de frecuencia 1!", Toast.LENGTH_SHORT).show();
            databaseHelper.increaseDaysCompleted(idHabit);
        }
    }

    /**
     * Verifica si tanto el CheckBox 1 como el CheckBox 2 están marcados y muestra un Toast si es así.
     *
     * @param checkBox1 El CheckBox 1 a verificar.
     * @param checkBox2 El CheckBox 2 a verificar.
     * @param idHabit El id del habito al que aumentarle el valor.
     */
    private void checkAndShowToastIfBothCheckBox1AndCheckBox2Checked(CheckBox checkBox1, CheckBox checkBox2, int idHabit) {
        if (checkBox1.isChecked() && checkBox2.isChecked()) {
            // Mostrar Toast cuando ambos CheckBox 1 y 2 están marcados
            Toast.makeText(context, "¡Enhorabuena por completar tu hábito de frecuencia 2!", Toast.LENGTH_SHORT).show();
            databaseHelper.increaseDaysCompleted(idHabit);
        }
    }

    /**
     * Verifica si todos los CheckBox están marcados y muestra un Toast si es así.
     *
     * @param checkBox1 El CheckBox 1 a verificar.
     * @param checkBox2 El CheckBox 2 a verificar.
     * @param checkBox3 El CheckBox 3 a verificar.
     * @param idHabit El id del habito al que aumentarle el valor.
     */
    private void checkAndShowToastIfAllChecked(CheckBox checkBox1, CheckBox checkBox2, CheckBox checkBox3, int idHabit) {
        boolean allChecked = checkBox1.isChecked() && (checkBox2 == null || checkBox2.isChecked()) && (checkBox3 == null || checkBox3.isChecked());
        if (allChecked) {
            // Mostrar Toast cuando todos los CheckBox están marcados
            Toast.makeText(context, "¡Enhorabuena por completar tu hábito de frecuencia 3!", Toast.LENGTH_SHORT).show();
            databaseHelper.increaseDaysCompleted(idHabit);
        }
    }

}