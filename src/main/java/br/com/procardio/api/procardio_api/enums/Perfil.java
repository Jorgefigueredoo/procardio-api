package br.com.procardio.api.procardio_api.enums;

public enum Perfil {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    PACIENTE("ROLE_PACIENTE");

    private String role;

    Perfil(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
