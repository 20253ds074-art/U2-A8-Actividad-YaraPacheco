package mx.edu.utez.proyecto1c.controller.ticket;


import jakarta.validation.Valid;
import mx.edu.utez.proyecto1c.controller.ticket.dto.RequestCrearTicketDTO;
import mx.edu.utez.proyecto1c.controller.ticket.dto.RequestReasignarPrioridadDTO;
import mx.edu.utez.proyecto1c.model.ticket.Ticket;
import mx.edu.utez.proyecto1c.service.ticket.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<Ticket> crearTicket(@Valid @RequestBody RequestCrearTicketDTO dto) {
        return new ResponseEntity<>(ticketService.crearTicket(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/avanzar-estado")
    public ResponseEntity<Ticket> actualizarEstadoTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.actualizarEstadoTicket(id));
    }

    @PutMapping("/{id}/prioridad")
    public ResponseEntity<Ticket> reasignarPrioridad(
            @PathVariable Long id,
            @Valid @RequestBody RequestReasignarPrioridadDTO dto) {
        return ResponseEntity.ok(ticketService.reasignarPrioridad(id, dto));
    }

    // 5. Obtener Tickets del Usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Ticket>> obtenerTicketsUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ticketService.obtenerTicketsUsuario(usuarioId));
    }
}