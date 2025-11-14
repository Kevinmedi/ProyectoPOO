package org.example.ProyectoPOO.modelo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.Hidden;

import javax.persistence.*;
import java.time.LocalDateTime;

    @Entity
    @Table(name = "evento_generado")
    @Getter
    @Setter
    public class EventoGenerado {

        @Id
        @Hidden
        @GeneratedValue(generator = "uuid-generator")
        @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
        @Column(name = "id_evento")
        private String idEvento;

        @Column(name = "tipo_evento", nullable = false, length = 50)
        private String tipoEvento;

        @Column(name = "fecha", nullable = false)
        private LocalDateTime fecha;

        @Column(name = "usuario_responsable", nullable = false, length = 100)
        private String usuarioResponsable;

        @Column(name = "detalle")
        private String detalle;

        // Constructores
        public EventoGenerado() {
        }

        // Método útil para auditoría
        public String resumen() {
            return String.format("Evento #%d [%s] por %s: %s", idEvento, tipoEvento, usuarioResponsable, fecha.toString());
        }
    }


