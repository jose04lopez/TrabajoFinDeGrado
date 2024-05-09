package com.principal.trabajofindegrado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Habits.db";
    private static final int DATABASE_VERSION = 5;

    // Tabla para los hábitos
    public static final String TABLE_NAME = "Habits";
    private static final String COLUMN_ID = "habit_id";
    public static final String COLUMN_TITLE = "habit_name";
    private static final String COLUMN_DIFFICULTY = "difficulty";
    private static final String COLUMN_FREQUENCY = "frequency";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_CHECKBOX1 = "checkbox1_status";
    private static final String COLUMN_CHECKBOX2 = "checkbox2_status";
    private static final String COLUMN_CHECKBOX3 = "checkbox3_status";

    // Tabla para los usuarios
    private static final String USER_TABLE_NAME = "Users";
    public static final String USER_COLUMN_ID = "user_id";
    private static final String USER_COLUMN_USERNAME = "username";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_BIRTHDATE = "birthdate";
    private static final String USER_COLUMN_EMAIL = "email";
    private static final String USER_COLUMN_PHONE = "phone";

    private final Context context;

    // Sentencia SQL para crear la tabla de hábitos
    private static final String CREATE_HABITS_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_ID + " INTEGER NOT NULL, " + // Nueva columna para el ID del usuario
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_DIFFICULTY + " TEXT NOT NULL, " +
            COLUMN_FREQUENCY + " INTEGER NOT NULL, " +
            COLUMN_START_DATE + " DATE NOT NULL, " +
            COLUMN_CHECKBOX1 + " INTEGER NOT NULL DEFAULT 0, " + // Nuevo checkbox 1
            COLUMN_CHECKBOX2 + " INTEGER NOT NULL DEFAULT 0, " + // Nuevo checkbox 2
            COLUMN_CHECKBOX3 + " INTEGER NOT NULL DEFAULT 0, " + // Nuevo checkbox 3
            "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COLUMN_ID + "));";

    // Sentencia SQL para crear la tabla de usuarios
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE_NAME + " (" +
            USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_COLUMN_USERNAME + " TEXT NOT NULL, " +
            USER_COLUMN_PASSWORD + " TEXT NOT NULL, " +
            USER_COLUMN_BIRTHDATE + " TEXT NOT NULL, " +
            USER_COLUMN_EMAIL + " TEXT NOT NULL, " +
            USER_COLUMN_PHONE + " TEXT NOT NULL);";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ejecutar la sentencia SQL para crear la tabla de hábitos
        db.execSQL(CREATE_HABITS_TABLE);
        // Ejecutar la sentencia SQL para crear la tabla de usuarios
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar las tablas si existen
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        // Volver a crear las tablas
        onCreate(db);
    }

    public void addUser(String username, String password, String dateOfBirth, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_COLUMN_USERNAME, username);
        cv.put(USER_COLUMN_PASSWORD, password);
        cv.put(USER_COLUMN_BIRTHDATE, dateOfBirth);
        cv.put(USER_COLUMN_EMAIL, email);
        cv.put(USER_COLUMN_PHONE, phone);
        long result = db.insert(USER_TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    public String getUsername(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(USER_COLUMN_USERNAME);
        if (columnIndex != -1) {
            return cursor.getString(columnIndex);
        }
        return null;
    }

    public String getPassword(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(USER_COLUMN_PASSWORD);
        if (columnIndex != -1) {
            return cursor.getString(columnIndex);
        }
        return null;
    }

    public ValidationResult validateCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {USER_COLUMN_ID, USER_COLUMN_USERNAME, USER_COLUMN_PASSWORD};
        String selection = USER_COLUMN_USERNAME + "=? AND " + USER_COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        User user = null;
        String userId = null; // Variable para almacenar el ID del usuario
        if (count > 0 && cursor.moveToFirst()) {
            int usernameIndex = cursor.getColumnIndex(USER_COLUMN_USERNAME);
            int passwordIndex = cursor.getColumnIndex(USER_COLUMN_PASSWORD);
            int userIdIndex = cursor.getColumnIndex(USER_COLUMN_ID);
            if (usernameIndex != -1 && passwordIndex != -1 && userIdIndex != -1) {
                String dbUsername = cursor.getString(usernameIndex);
                String dbPassword = cursor.getString(passwordIndex);
                userId = cursor.getString(userIdIndex); // Obtener el ID del usuario
                user = new User(dbUsername, dbPassword);
            }
        }
        cursor.close();
        return new ValidationResult(count > 0, user, userId);
    }



    public void deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(USER_TABLE_NAME, USER_COLUMN_USERNAME + "=?", new String[]{username});
        if (result == -1) {
            Toast.makeText(context, "Error al eliminar la cuenta de usuario", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Cuenta de usuario eliminada correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    public void addHabit(String userId, String name, String difficulty, int frequency, String startDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, userId);
        cv.put(COLUMN_TITLE, name);
        cv.put(COLUMN_DIFFICULTY, difficulty);
        cv.put(COLUMN_FREQUENCY, frequency);
        cv.put(COLUMN_START_DATE, startDate);
        cv.put(COLUMN_CHECKBOX1, 0); // Valor predeterminado del checkbox 1
        cv.put(COLUMN_CHECKBOX2, 0); // Valor predeterminado del checkbox 2
        cv.put(COLUMN_CHECKBOX3, 0); // Valor predeterminado del checkbox 3

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Error al añadir el hábito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Hábito añadido correctamente", Toast.LENGTH_SHORT).show();
        }
    }


    public Cursor readAllHabits(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, COLUMN_USER_ID + "=?", new String[]{userId}, null, null, null);
    }

    public void updateHabit(Habit habit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, habit.getName());
        cv.put(COLUMN_DIFFICULTY, habit.getDifficulty());
        cv.put(COLUMN_FREQUENCY, habit.getFrequency());
        cv.put(COLUMN_START_DATE, habit.getStartDate());

        int result = db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{String.valueOf(habit.getId())});
        if(result == -1){
            Toast.makeText(context, "Error al actualizar el hábito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Hábito actualizado correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteHabit(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Error al eliminar el hábito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Hábito eliminado correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    public void changePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Obtener la contraseña actual del usuario
        String currentPassword = getPasswordByUsername(username);

        // Verificar si la nueva contraseña es diferente de la contraseña actual
        if (!newPassword.equals(currentPassword)) {
            ContentValues cv = new ContentValues();
            cv.put(USER_COLUMN_PASSWORD, newPassword);
            int result = db.update(USER_TABLE_NAME, cv, USER_COLUMN_USERNAME + "=?", new String[]{username});
            if (result == -1) {
                Toast.makeText(context, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Mostrar un mensaje de error si la nueva contraseña es igual a la contraseña actual
            Toast.makeText(context, "La nueva contraseña debe ser diferente de la contraseña actual", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para obtener la contraseña actual del usuario
    public String getPasswordByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {USER_COLUMN_PASSWORD};
        String selection = USER_COLUMN_USERNAME + "=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            int passwordIndex = cursor.getColumnIndex(USER_COLUMN_PASSWORD);
            if (passwordIndex != -1) {
                password = cursor.getString(passwordIndex);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return password;
    }

    // Método para actualizar el estado de un checkbox en la base de datos
    public void updateCheckboxStatus(int habitId, int checkboxNumber, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String columnToUpdate;
        switch (checkboxNumber) {
            case 1:
                columnToUpdate = COLUMN_CHECKBOX1;
                break;
            case 2:
                columnToUpdate = COLUMN_CHECKBOX2;
                break;
            case 3:
                columnToUpdate = COLUMN_CHECKBOX3;
                break;
            default:
                // Si el número de checkbox es inválido, no hacer nada
                return;
        }

        cv.put(columnToUpdate, status);
        int result = db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{String.valueOf(habitId)});
        if (result == -1) {
            Toast.makeText(context, "Error al actualizar el estado del checkbox", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para reiniciar el estado de los checkboxes
    public void resetCheckboxStatus() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CHECKBOX1, 0); // Reiniciar el estado del checkbox 1
        cv.put(COLUMN_CHECKBOX2, 0); // Reiniciar el estado del checkbox 2
        cv.put(COLUMN_CHECKBOX3, 0); // Reiniciar el estado del checkbox 3
        int result = db.update(TABLE_NAME, cv, null, null);
        if (result == -1) {
            Toast.makeText(context, "Error al reiniciar el estado de los checkboxes", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Estado de los checkboxes reiniciado correctamente", Toast.LENGTH_SHORT).show();
        }
    }


}
