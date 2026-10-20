package mx.edu.utez.proyecto1c.service.ticket;


import mx.edu.utez.proyecto1c.model.ticket.EstadoTicket;
import mx.edu.utez.proyecto1c.model.ticket.PrioridadTicket;
import mx.edu.utez.proyecto1c.model.ticket.Ticket;
import mx.edu.utez.proyecto1c.model.usuario.Usuario;
import mx.edu.utez.proyecto1c.repository.ticket.TicketRepository;
import mx.edu.utez.proyecto1c.repository.usuario.UsuarioRepository;
import mx.edu.utez.proyecto1c.controller.ticket.dto.RequestCrearTicketDTO;
import mx.edu.utez.proyecto1c.controller.ticket.dto.RequestReasignarPrioridadDTO;
import mx.edu.utez.proyecto1c.exception.customExceptions.BadRequestException;
import mx.edu.utez.proyecto1c.exception.customExceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;

    public TicketService(TicketRepository ticketRepository, UsuarioRepository usuarioRepository) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Ticket crearTicket(RequestCrearTicketDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new NotFoundException("El usuario no existe"));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new BadRequestException("El usuario se encuentra inactivo. No puede crear tickets.");
        }

        Ticket ticket = new Ticket();
        ticket.setDescripcion(dto.getDescripcion());
        ticket.setEstado(EstadoTicket.ABIERTO); // Estado inicial ABIERTO
        ticket.setPrioridad(PrioridadTicket.valueOf(dto.getPrioridad()));
        ticket.setUsuario(usuario);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket actualizarEstadoTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("El ticket no existe"));

        if (ticket.getEstado() == EstadoTicket.ABIERTO) {
            ticket.setEstado(EstadoTicket.EN_PROCESO);
        } else if (ticket.getEstado() == EstadoTicket.EN_PROCESO) {
            ticket.setEstado(EstadoTicket.CERRADO);
        } else if (ticket.getEstado() == EstadoTicket.CERRADO) {
            throw new BadRequestException("El ticket ya se encuentra CERRADO y no puede ser modificado.");
        }

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket reasignarPrioridad(Long ticketId, RequestReasignarPrioridadDTO dto) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("El ticket no existe"));

        if (ticket.getEstado() == EstadoTicket.CERRADO) {
            throw new BadRequestException("No se puede cambiar la prioridad a un ticket que está CERRADO.");
        }

        ticket.setPrioridad(PrioridadTicket.valueOf(dto.getNuevaPrioridad()));
        return ticketRepository.save(ticket);
    }

    public List<Ticket> obtenerTicketsUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new NotFoundException("El usuario no existe");
        }
        return ticketRepository.findByUsuarioId(usuarioId);
    }
}
