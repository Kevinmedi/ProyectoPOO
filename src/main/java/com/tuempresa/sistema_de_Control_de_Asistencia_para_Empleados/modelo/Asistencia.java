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
            LocalDate fechaActual = fechaHora.toLocalDate();
            LocalTime horaActual = fechaHora.toLocalTime();
            LocalTime horaEntradaFija = LocalTime.of(8, 0);   // 8:00 a.m.
            LocalTime horaSalidaFija = LocalTime.of(17, 0);   // 5:00 p.m.

            // ? Validar que no exista otra ENTRADA el mismo día
            if (estado == EstadoAsistencia.ENTRADA) {
                Long conteoEntrada = (Long) XPersistence.getManager()
                        .createQuery("SELECT COUNT(a) FROM Asistencia a WHERE a.empleado = :empleado AND a.estado = :estado AND DATE(a.fechaHora) = :fecha")
                        .setParameter("empleado", empleado)
                        .setParameter("estado", EstadoAsistencia.ENTRADA)
                        .setParameter("fecha", fechaActual)
                        .getSingleResult();

                if (conteoEntrada > 0) {
                    throw new RuntimeException("El empleado ya marcó ENTRADA en la fecha " + fechaActual + ". No puede registrar otra entrada.");
                }

                // Clasificación de entrada
                this.clasificacion = horaActual.isAfter(horaEntradaFija) ?
                        ClasificacionAsistencia.RETRASO :
                        ClasificacionAsistencia.PUNTUAL;
            }

            // ? Validar que no exista otra SALIDA el mismo día
            if (estado == EstadoAsistencia.SALIDA) {
                Long conteoSalida = (Long) XPersistence.getManager()
                        .createQuery("SELECT COUNT(a) FROM Asistencia a WHERE a.empleado = :empleado AND a.estado = :estado AND DATE(a.fechaHora) = :fecha")
                        .setParameter("empleado", empleado)
                        .setParameter("estado", EstadoAsistencia.SALIDA)
                        .setParameter("fecha", fechaActual)
                        .getSingleResult();

                if (conteoSalida > 0) {
                    throw new RuntimeException("El empleado ya marcó SALIDA en la fecha " + fechaActual + ". No puede registrar otra salida.");
                }

                // Clasificación de salida y cálculo de horas extras
                this.clasificacion = horaActual.isBefore(horaSalidaFija) ?
                        ClasificacionAsistencia.RETRASO :
                        ClasificacionAsistencia.PUNTUAL;

                if (horaActual.isAfter(horaSalidaFija)) {
                    this.horasExtras = Duration.between(horaSalidaFija, horaActual).toMinutes() / 60.0;
                } else {
                    this.horasExtras = 0;
                }
            }
        }
    }



}