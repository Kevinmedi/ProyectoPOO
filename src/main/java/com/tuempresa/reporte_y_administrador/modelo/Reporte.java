package com.tuempresa.reporte_y_administrador.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Getter
@Setter
public class Reporte {

    @Id
    @Hidden
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    private String id;

    @Column(name = "codigo_reporte", nullable = false, length = 10)
    @Size(min = 1, max = 10, message = "El código debe tener entre 1 y 10 caracteres.")
    @Required(message = "El campo código debe ser ingresado.")
    private String codigo;

    @Column(name = "tipo_reporte", nullable = false, length = 60)
    @Required(message = "El campo tipo debe ser ingresado.")
    private String tipo;

    @Column(name = "datos_reporte", nullable = false)
    @Required(message = "El campo datos debe ser ingresado.")
    private String datos;

    @Column(name = "fecha_generacion", nullable = false)
    @Required(message = "El campo fecha de generación debe ser ingresado.")
    private LocalDateTime fechaGeneracion;
}

