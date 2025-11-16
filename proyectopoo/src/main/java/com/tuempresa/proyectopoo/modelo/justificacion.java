package com.tuempresa.proyectopoo.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import org.openxava.annotations.TextArea;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "justificaciones")
@Getter
@Setter
public class justificacion {

    @Id
    @Hidden
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    private String id;

    @Column(name = "motivo", nullable = false, length = 100)
    @Size(min = 3, max = 100, message = "El motivo debe tener entre 3 y 100 caracteres.")
    @Required(message = "Debe ingresar un motivo.")
    private String motivo;

    @Column(name = "fecha_solicitud", nullable = false)
    @Required(message = "Debe ingresar la fecha de solicitud.")
    private LocalDateTime fechaSolicitud;

    @Column(name = "estado", nullable = false, length = 20)
    @Required(message = "Debe ingresar el estado de la justificación.")
    private String estado;
    @TextArea
    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "documento_adjunto", length = 255)
    private String documentoAdjunto;

    @Column(name = "empleado_id", length = 36)
    private String empleadoId;

    @Column(name = "asistencia_id", length = 36)
    private String asistenciaId;
}