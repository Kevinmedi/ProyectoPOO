package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import org.openxava.jpa.XPersistence;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "asistencia")
@Getter @Setter
@View(members =
        "DatosDeAsistencia {" +          // nombre de sección SIN espacios
                "   codigoEmpleado;" +
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

    // -------- CAMPO QUE ESCRIBE EL EMPLEADO (NO VA A BD) --------
    @Transient
    @Required(message = "El código es obligatorio.")
    @Size(min = 8, max = 8, message = "El código debe tener 8 dígitos.")
    @Pattern(regexp = "\\d{8}", message = "El código solo puede contener números.")
    @DisplaySize(8)
    private String codigoEmpleado;

    // -------- EMPLEADO REAL (SE LLENA DESDE EL CÓDIGO) --------
    @ManyToOne(optional = true)   // en BD no puede ser null
    @ReadOnly                      // el usuario no lo puede cambiar
    private Empleado empleado;

    // -------- CAMPOS AUTOMÁTICOS --------
    @Column(nullable = false)
    @ReadOnly
    private LocalDate fecha;

    @Column
    @ReadOnly
    private LocalTime horaEntrada;

    @Column
    @ReadOnly
    private LocalTime horaSalida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ReadOnly
    private EstadoAsistencia estado;   // PUNTUAL, TARDE, AUSENTE

    // ================== LÓGICA AUTOMÁTICA ==================

    @PrePersist
    private void alCrear() {
        // 1) Buscar el empleado por el código que escribió el usuario
        asignarEmpleadoDesdeCodigo();

        // 2) Setear fecha y hora de entrada
        fecha = LocalDate.now();
        horaEntrada = LocalTime.now();

        // 3) Calcular estado según la hora de entrada
        actualizarEstado();
    }

    @PreUpdate
    private void alActualizar() {
        // Cuando editen para marcar salida
        if (horaSalida == null) {
            horaSalida = LocalTime.now();
        }
        actualizarEstado();
    }

    // --- Buscar empleado en la BD usando el código digitado ---
    private void asignarEmpleadoDesdeCodigo() {
        if (codigoEmpleado == null || codigoEmpleado.isEmpty()) {
            throw new RuntimeException("Debe ingresar el código del empleado");
        }

        try {
            Empleado e = XPersistence.getManager()
                    .createQuery("from Empleado e where e.codigo = :codigo", Empleado.class)
                    .setParameter("codigo", codigoEmpleado)
                    .getSingleResult();
            this.empleado = e;
        } catch (Exception ex) {
            throw new RuntimeException("No existe un empleado con código " + codigoEmpleado);
        }
    }

    // --- Calcular si está puntual, tarde o ausente ---
    private void actualizarEstado() {
        LocalTime entradaLimite = LocalTime.of(7, 0);   // 7:00 am

        if (horaEntrada == null) {
            this.estado = EstadoAsistencia.AUSENTE;
            return;
        }

        if (horaEntrada.isAfter(entradaLimite)) {
            this.estado = EstadoAsistencia.RETRASO;
        } else {
            this.estado = EstadoAsistencia.PUNTUAL;
        }
    }
}
