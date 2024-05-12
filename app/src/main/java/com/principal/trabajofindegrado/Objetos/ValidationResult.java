package com.principal.trabajofindegrado.Objetos;


import com.principal.trabajofindegrado.Objetos.User;

/**
 * Clase que representa el resultado de la validación de un usuario.
 * Contiene información sobre si la validación fue exitosa, el usuario validado y su ID.
 *
 * @author Jose y Guillermo
 * @version 1.0
 */
public class ValidationResult {
    private final boolean isValid;
    private final String userId;

    /**
     * Constructor de la clase ValidationResult.
     *
     * @param isValid  Indica si la validación fue exitosa.
     * @param user     El usuario validado.
     * @param userId   El ID del usuario validado.
     */
    public ValidationResult(boolean isValid, User user, String userId) {
        this.isValid = isValid;
        this.userId = userId;
    }

    /**
     * Verifica si la validación fue exitosa.
     *
     * @return true si la validación fue exitosa, de lo contrario false.
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Obtiene el ID del usuario validado.
     *
     * @return El ID del usuario validado.
     */
    public String getUserId() {
        return userId;
    }
}
