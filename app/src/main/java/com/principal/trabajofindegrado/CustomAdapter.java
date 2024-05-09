package com.principal.trabajofindegrado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Habit> habitList;
    private OnHabitClickListener onHabitClickListener;
    private MyDatabaseHelper databaseHelper;

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

    // Método para establecer el helper de la base de datos
    public void setDatabaseHelper(MyDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
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
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habitList.get(position);

        // Set habit name
        holder.txtHabitName.setText(habit.getName());

        // Set difficulty with resource string
        holder.txtDifficulty.setText(context.getString(R.string.difficulty_label) + habit.getDifficulty());

        // Set frequency with resource string
        holder.txtFrequency.setText(context.getString(R.string.frequency_label) + habit.getFrequency());

        // Set start date with resource string
        holder.txtStartDate.setText(context.getString(R.string.start_date_label) + habit.getStartDate());

        // Set checkboxes visibility based on frequency
        switch (habit.getFrequency()) {
            case 1:
                holder.checkBox1.setVisibility(View.VISIBLE);
                holder.checkBox2.setVisibility(View.GONE);
                holder.checkBox3.setVisibility(View.GONE);
                holder.checkBox1.setChecked(habit.getCheckbox1Status() == 1);
                // Agregar lógica para verificar y mostrar Toast si todos los CheckBox están marcados
                holder.checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    habit.setCheckbox1Status(isChecked ? 1 : 0);
                    databaseHelper.updateCheckboxStatus(habit.getId(), 1, isChecked ? 1 : 0);
                    checkAndShowToastIfOnlyCheckBox1Checked(holder.checkBox1);
                });
                break;
            case 2:
                holder.checkBox1.setVisibility(View.VISIBLE);
                holder.checkBox2.setVisibility(View.VISIBLE);
                holder.checkBox3.setVisibility(View.GONE);
                holder.checkBox1.setChecked(habit.getCheckbox1Status() == 1);
                holder.checkBox2.setChecked(habit.getCheckbox2Status() == 1);
                // Agregar lógica para verificar y mostrar Toast si todos los CheckBox están marcados
                holder.checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    habit.setCheckbox1Status(isChecked ? 1 : 0);
                    databaseHelper.updateCheckboxStatus(habit.getId(), 1, isChecked ? 1 : 0);
                    checkAndShowToastIfBothCheckBox1AndCheckBox2Checked(holder.checkBox1, holder.checkBox2);
                });

                holder.checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    habit.setCheckbox2Status(isChecked ? 1 : 0);
                    databaseHelper.updateCheckboxStatus(habit.getId(), 2, isChecked ? 1 : 0);
                    checkAndShowToastIfAllChecked(holder.checkBox1, holder.checkBox2, holder.checkBox3);
                });
                break;
            case 3:
                holder.checkBox1.setVisibility(View.VISIBLE);
                holder.checkBox2.setVisibility(View.VISIBLE);
                holder.checkBox3.setVisibility(View.VISIBLE);
                holder.checkBox1.setChecked(habit.getCheckbox1Status() == 1);
                holder.checkBox2.setChecked(habit.getCheckbox2Status() == 1);
                holder.checkBox3.setChecked(habit.getCheckbox3Status() == 1);
                // Agregar lógica para mostrar Toast cuando los tres CheckBox estén marcados
                holder.checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    habit.setCheckbox1Status(isChecked ? 1 : 0);
                    databaseHelper.updateCheckboxStatus(habit.getId(), 1, isChecked ? 1 : 0);
                    checkAndShowToastIfAllChecked(holder.checkBox1, holder.checkBox2, holder.checkBox3);
                });

                holder.checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    habit.setCheckbox2Status(isChecked ? 1 : 0);
                    databaseHelper.updateCheckboxStatus(habit.getId(), 2, isChecked ? 1 : 0);
                    checkAndShowToastIfAllChecked(holder.checkBox1, holder.checkBox2, holder.checkBox3);
                });

                holder.checkBox3.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    habit.setCheckbox3Status(isChecked ? 1 : 0);
                    databaseHelper.updateCheckboxStatus(habit.getId(), 3, isChecked ? 1 : 0);
                    checkAndShowToastIfAllChecked(holder.checkBox1, holder.checkBox2, holder.checkBox3);
                });
                break;
            default:
                break;
        }

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
        CheckBox checkBox1, checkBox2, checkBox3;

        // Constructor ViewHolder
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

    // Método para verificar si solo el CheckBox 1 está marcado y mostrar Toast
    private void checkAndShowToastIfOnlyCheckBox1Checked(CheckBox checkBox1) {
        if (checkBox1.isChecked()) {
            // Mostrar Toast cuando solo el CheckBox 1 está marcado
            Toast.makeText(context, "¡Enhorabuena por completar tu hábito de frecuencia 1!", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para verificar si todos los CheckBox 1 y 2 están marcados y mostrar Toast
    private void checkAndShowToastIfBothCheckBox1AndCheckBox2Checked(CheckBox checkBox1, CheckBox checkBox2) {
        if (checkBox1.isChecked() && checkBox2.isChecked()) {
            // Mostrar Toast cuando los CheckBox 1 y 2 están marcados
            Toast.makeText(context, "¡Enhorabuena por completar tu hábito de frecuencia 2!", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para verificar si los tres CheckBox están marcados y mostrar Toast
    private void checkAndShowToastIfAllChecked(CheckBox checkBox1, CheckBox checkBox2, CheckBox checkBox3) {
        boolean allChecked = checkBox1.isChecked() && (checkBox2 == null || checkBox2.isChecked()) && (checkBox3 == null || checkBox3.isChecked());
        if (allChecked) {
            // Mostrar Toast cuando todos los CheckBox están marcados
            Toast.makeText(context, "¡Enhorabuena por completar tu hábito de frecuencia 3!", Toast.LENGTH_SHORT).show();
        }
    }

}
