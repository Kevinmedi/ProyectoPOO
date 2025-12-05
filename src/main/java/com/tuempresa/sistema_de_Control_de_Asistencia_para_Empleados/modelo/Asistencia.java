package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import org.openxava.jpa.XPersistence;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "asistencia")
@Getter @Setter
@View(members =
        "Datos de Asistencia {" +
                " empleado; estado; fechaHora" +
                "}"
)
@Tab(properties = "empleado.codigo, empleado.nombre, estado, fechaHora")
public class Asistencia {

    @Id
    @Hidden
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    private String id;

    @ManyToOne(optional = false)
    @Required(message = "Debe seleccionar un empleado.")
    @DescriptionsList(descriptionProperties="codigo, nombre, apellido")
    private Empleado empleado;

    @Column
    @ReadOnly
    @DefaultValueCalculator(CurrentDateTimeCalculator.class)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Required(message = "Debe seleccionar si está entrando o saliendo.")
    private EstadoAsistencia estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClasificacionAsistencia clasificacion;

    @Column
    private double horasExtras;

    @PrePersist
    public void validarEmpleado() {
        if (empleado == null) {
            throw new RuntimeException("Empleado no asignado. Verifica que el código ingresado sea válido.");
        }
        if (fechaHora != null) {
            LocalTime horaActual = fechaHora.toLocalTime();
            LocalTime horaEntradaFija = LocalTime.of(8, 0);  // 8:00 a.m.
            LocalTime horaSalidaFija = LocalTime.of(12, 0);  // 5:00 p.m.

            // Clasificación de entrada
            if (estado == EstadoAsistencia.ENTRADA) {
                this.clasificacion = horaActual.isAfter(horaEntradaFija) ?
                        ClasificacionAsistencia.RETRASO :
                        ClasificacionAsistencia.PUNTUAL;
            }

            // Clasificación de salida y cálculo de horas extras
            if (estado == EstadoAsistencia.SALIDA) {
                this.clasificacion = horaActual.isBefore(horaSalidaFija) ?
                        ClasificacionAsistencia.RETRASO :
                        ClasificacionAsistencia.PUNTUAL;

                if (horaActual.isAfter(horaSalidaFija)) {
                    // Calcular horas extras en minutos convertidos a horas decimales
                    this.horasExtras = Duration.between(horaSalidaFija, horaActual).toMinutes() / 60.0;
                } else {
                    this.horasExtras = 0;
                }
            }
        }



    }



}