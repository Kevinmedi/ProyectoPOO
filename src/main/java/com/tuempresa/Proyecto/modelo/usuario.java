package com.tuempresa.Proyecto.modelo;

import javax.persistence.*;
import org.openxava.model.*;

@Entity
public class usuario extends Identifiable {

    @Column(length = 30)
    private String nombreUsuario;

    @Column(length = 30)
    private String contrasena;

    @Column(length = 30)
    private String rol;

    @OneToOne(fetch = FetchType.LAZY)
    private empleado empleado;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(empleado empleado) {
        this.empleado = empleado;
    }
}