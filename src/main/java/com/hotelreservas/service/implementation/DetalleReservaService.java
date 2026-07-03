package com.hotelreservas.service.implementation;

import com.hotelreservas.model.DetalleReserva;
import com.hotelreservas.repository.IDetalleReservaRepository;
import com.hotelreservas.service.IDetalleReservaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleReservaService extends GenericServiceImpl<DetalleReserva, Long> implements IDetalleReservaService {

    private final IDetalleReservaRepository detalleReservaRepository;

    public DetalleReservaService(IDetalleReservaRepository detalleReservaRepository) {
        super(detalleReservaRepository, "Detalle de reserva");
        this.detalleReservaRepository = detalleReservaRepository;
    }

    @Override
    public List<DetalleReserva> listarPorReserva(Long reservaId) {
        return detalleReservaRepository.findByReservaId(reservaId);
    }

    // guardar(), actualizar() y eliminar() heredan el comportamiento genérico de
    // GenericServiceImpl. La creación/actualización real de detalles se gestiona
    // a través de ReservaService, ya que un detalle siempre pertenece a una reserva.
}
