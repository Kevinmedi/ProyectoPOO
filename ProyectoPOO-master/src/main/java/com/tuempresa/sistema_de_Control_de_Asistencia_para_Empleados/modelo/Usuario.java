package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo;
import javax.persistence.*;
import lombok.*;
import org.openxava.annotations.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "usuario")
@Getter @Setter
@View(members =
        "identificacion {" +
                "id;" +
                "nombreUsuario;" +
                "}" +
                "credenciales {" +
                "clave;" +
                "rol;" +
                "}" +
                "relacion {" +
                "empleado;" +
                "}"
)
public class Usuario {

    // 1. id (Clave Primaria auto-generada)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden // OpenXava: Oculta el ID en la vista ya que se genera automáticamente
    private Long id;

    // 2. username (Nombre de usuario único para login)
    @Column(length = 32, unique = true) // 'unique = true' asegura que no haya duplicados
    @Required
    private String nombreUsuario;

    // 3. password (Clave)
    @Column(length = 64)
    @Required
    @Length(min=6, message="La clave debe tener al menos 6 caracteres")
    @Stereotype("PASSWORD") // OpenXava: Oculta la clave en la interfaz
    private String clave;

    // 4. rol
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Required
    private Rol rol;

}
