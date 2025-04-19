package com.sistema.clinica.models.dtos;

public class EditarPerfilForm {
    private String email;
    private String password;
    private String newPassword;


    public EditarPerfilForm() {
    }

    public EditarPerfilForm(String email, String password, String newPassword) {
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}