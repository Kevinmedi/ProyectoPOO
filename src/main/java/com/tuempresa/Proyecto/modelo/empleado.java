package com.tuempresa.Proyecto.modelo;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Tab(properties="id, nombre, cargo, horario")
@View(members=
        "id; nombre; cargo; horario"
)
public class empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable=false, unique=true)
    private Long id;

    @Required
    @Column(length=50, nullable=false)
    private String nombre;

    @Required
    @Column(length=50, nullable=false)
    private String cargo;

    @Required
    @Column(length=50, nullable=false)
    private String horario;
}
