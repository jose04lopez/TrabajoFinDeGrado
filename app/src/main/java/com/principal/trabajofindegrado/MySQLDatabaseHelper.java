package com.principal.trabajofindegrado;

import android.content.Context;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLDatabaseHelper {

    private static Connection conn;
    private Context context;

    public MySQLDatabaseHelper(Context context) {
        this.context = context;
        try {
            conn = MySQLConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para añadir un usuario a la base de datos
    public void addUser(String username, String password, String dateOfBirth, String email, String phone) {
        try {
            // Preparar la consulta SQL
            String sql = "INSERT INTO Users (username, password, birthdate, email, phone) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, dateOfBirth);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);

            // Ejecutar la consulta SQL
            int result = pstmt.executeUpdate();

            // Verificar el resultado de la inserción
            if (result == 1) {
                Toast.makeText(context, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
            }

            // Cerrar el PreparedStatement
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ValidationResult validateCredentials(String username, String password) {
        try {
            // Preparar la consulta SQL
            String sql = "SELECT user_id, username, password FROM Users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // Ejecutar la consulta SQL
            ResultSet rs = pstmt.executeQuery();

            // Procesar el resultado
            int count = 0;
            User user = null;
            String userId = null;
            if (rs.next()) {
                userId = rs.getString("user_id");
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password");
                user = new User(dbUsername, dbPassword);
                count = 1;
            }

            // Cerrar el PreparedStatement y ResultSet
            rs.close();
            pstmt.close();

            return new ValidationResult(count > 0, user, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteUser(String username) {
        try {
            // Preparar la consulta SQL
            String sql = "DELETE FROM Users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            // Ejecutar la consulta SQL
            int result = pstmt.executeUpdate();

            // Verificar el resultado de la eliminación
            if (result == 1) {
                Toast.makeText(context, "Cuenta de usuario eliminada correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al eliminar la cuenta de usuario", Toast.LENGTH_SHORT).show();
            }

            // Cerrar el PreparedStatement
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addHabit(String userId, String name, String difficulty, int frequency, String startDate) {
        try {
            // Preparar la consulta SQL
            String sql = "INSERT INTO Habits (user_id, habit_name, difficulty, frequency, start_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, name);
            pstmt.setString(3, difficulty);
            pstmt.setInt(4, frequency);
            pstmt.setString(5, startDate);

            // Ejecutar la consulta SQL
            int result = pstmt.executeUpdate();

            // Verificar el resultado de la inserción
            if (result == 1) {
                Toast.makeText(context, "Hábito añadido correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al añadir el hábito", Toast.LENGTH_SHORT).show();
            }

            // Cerrar el PreparedStatement
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet readAllHabits(String userId) {
        try {
            // Preparar la consulta SQL
            String sql = "SELECT * FROM Habits WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);

            // Ejecutar la consulta SQL
            ResultSet rs = pstmt.executeQuery();

            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateHabit(Habit habit) {
        try {
            // Preparar la consulta SQL
            String sql = "UPDATE Habits SET habit_name = ?, difficulty = ?, frequency = ?, start_date = ? WHERE habit_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, habit.getName());
            pstmt.setString(2, habit.getDifficulty());
            pstmt.setInt(3, habit.getFrequency());
            pstmt.setString(4, habit.getStartDate());
            pstmt.setInt(5, habit.getId());

            // Ejecutar la consulta SQL
            int result = pstmt.executeUpdate();

            // Verificar el resultado de la actualización
            if (result == 1) {
                Toast.makeText(context, "Hábito actualizado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al actualizar el hábito", Toast.LENGTH_SHORT).show();
            }

            // Cerrar el PreparedStatement
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteHabit(String id) {
        try {
            // Preparar la consulta SQL
            String sql = "DELETE FROM Habits WHERE habit_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            // Ejecutar la consulta SQL
            int result = pstmt.executeUpdate();

            // Verificar el resultado de la eliminación
            if (result == 1) {
                Toast.makeText(context, "Hábito eliminado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al eliminar el hábito", Toast.LENGTH_SHORT).show();
            }

            // Cerrar el PreparedStatement
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(String username, String newPassword) {
        try {
            // Obtener la contraseña actual del usuario
            String currentPassword = getPasswordByUsername(username);

            // Verificar si la nueva contraseña es diferente de la contraseña actual
            if (!newPassword.equals(currentPassword)) {
                // Preparar la consulta SQL para actualizar la contraseña
                String sql = "UPDATE Users SET password = ? WHERE username = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, newPassword);
                pstmt.setString(2, username);

                // Ejecutar la consulta SQL
                int result = pstmt.executeUpdate();

                // Verificar el resultado de la actualización
                if (result == 1) {
                    Toast.makeText(context, "Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                }

                // Cerrar el PreparedStatement
                pstmt.close();
            } else {
                // Mostrar un mensaje de error si la nueva contraseña es igual a la contraseña actual
                Toast.makeText(context, "La nueva contraseña debe ser diferente de la contraseña actual", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPasswordByUsername(String username) {
        try {
            // Preparar la consulta SQL
            String sql = "SELECT password FROM Users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            // Ejecutar la consulta SQL
            ResultSet rs = pstmt.executeQuery();

            // Obtener la contraseña si existe
            String password = null;
            if (rs.next()) {
                password = rs.getString("password");
            }

            // Cerrar el PreparedStatement y ResultSet
            rs.close();
            pstmt.close();

            return password;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateCheckboxStatus(int habitId, int checkboxNumber, int status) {
        try {
            // Preparar el nombre de la columna a actualizar según el número de checkbox
            String columnToUpdate;
            switch (checkboxNumber) {
                case 1:
                    columnToUpdate = "checkbox1_status";
                    break;
                case 2:
                    columnToUpdate = "checkbox2_status";
                    break;
                case 3:
                    columnToUpdate = "checkbox3_status";
                    break;
                default:
                    // Si el número de checkbox es inválido, no hacer nada
                    return;
            }

            // Preparar la consulta SQL
            String sql = "UPDATE Habits SET " + columnToUpdate + " = ? WHERE habit_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setInt(2, habitId);

            // Ejecutar la consulta SQL
            int result = pstmt.executeUpdate();

            // Verificar el resultado de la actualización
            if (result == 1) {
                Toast.makeText(context, "Estado del checkbox actualizado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al actualizar el estado del checkbox", Toast.LENGTH_SHORT).show();
            }

            // Cerrar el PreparedStatement
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetCheckboxStatus() {
        try {
            // Preparar la consulta SQL
            String sql = "UPDATE Habits SET checkbox1_status = 0, checkbox2_status = 0, checkbox3_status = 0";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Ejecutar la consulta SQL
            int result = pstmt.executeUpdate();

            // Verificar el resultado de la actualización
            if (result >= 0) {
                Toast.makeText(context, "Estado de los checkboxes reiniciado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al reiniciar el estado de los checkboxes", Toast.LENGTH_SHORT).show();
            }

            // Cerrar el PreparedStatement
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}