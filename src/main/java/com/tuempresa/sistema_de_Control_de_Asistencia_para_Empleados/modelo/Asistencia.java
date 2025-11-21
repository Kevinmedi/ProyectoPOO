package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "asistencia")
@Getter @Setter
@View(members =
        "Datos de Asistencia {" +
                "   empleado;" +
                "   fecha;" +
                "   horaEntrada; horaSalida;" +
                "   estado;" +
                "}"
)
@Tab(properties = "empleado.codigo, empleado.nombre, fecha, horaEntrada, horaSalida, estado")
public class Asistencia {

    @Id
    @Hidden
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    private String id;

    @ManyToOne(optional = false)
    @Required(message = "Debe seleccionar un empleado.")
    private Empleado empleado;

    @Column(nullable = false)
    @Required(message = "La fecha es obligatoria.")
    private LocalDate fecha;

    @Column
    private LocalTime horaEntrada;

    @Column
    private LocalTime horaSalida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Required(message = "Debe seleccionar un estado.")
    private EstadoAsistencia estado;
}
