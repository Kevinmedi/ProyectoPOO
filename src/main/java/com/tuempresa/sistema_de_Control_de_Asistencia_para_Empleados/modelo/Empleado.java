package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Optional;

@Entity
@Table(name = "empleados")
@Getter @Setter
@View(members =
        "Información General {" +
                "   codigo; nombre; apellido; " +
                "   cargo;" +
                "}" +
                "Contacto {" +
                "   telefono; email;" +
                "}"
)
@Tab(properties = "codigo, nombre, apellido, cargo")
public class Empleado {

    @Id
    @Hidden
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    private String id;

    @SearchKey
    @Column(length = 7, nullable = false)
    @Required(message = "El código es obligatorio.")
    private String codigo;

    @Column(length = 30, nullable = false)
    @Required(message = "El nombre es obligatorio.")
    private String nombre;

    @Column(length = 30, nullable = false)
    @Required(message = "El apellido es obligatorio.")
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Required(message = "Debe seleccionar un cargo para el empleado.")
    private EmpleadoCargo cargo;



    @Column(length = 15)
    @Stereotype("TELEPHONE")
    private String telefono;

    @Column(length = 50)
    @EmailList
    private String email;
}

