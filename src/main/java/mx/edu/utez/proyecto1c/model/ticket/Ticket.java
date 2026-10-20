package mx.edu.utez.proyecto1c.model.ticket;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.proyecto1c.model.usuario.Usuario;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoTicket estado;

    @Enumerated(EnumType.STRING)
    private PrioridadTicket prioridad;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}