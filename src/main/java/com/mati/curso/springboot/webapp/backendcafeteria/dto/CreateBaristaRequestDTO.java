package com.mati.curso.springboot.webapp.backendcafeteria.dto;

public class CreateBaristaRequestDTO {
    private String email;
    private String password;

    public CreateBaristaRequestDTO() {}

    public CreateBaristaRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
} 