package com.upc.acosta.app.service;

import com.upc.acosta.app.dto.CodigoDTOResponse;
import com.upc.acosta.app.entity.CodigoOperacion;
import com.upc.acosta.app.entity.SecuenciaCodigo;
import com.upc.acosta.app.entity.Usuario;
import com.upc.acosta.app.entity.tipos.EstadoReserva;
import com.upc.acosta.app.repository.ICodigoOperacionRepository;
import com.upc.acosta.app.repository.ISecuenciaCodigoRepository;
import com.upc.acosta.app.repository.IUsuarioRepository;
import com.upc.acosta.app.service.interfaces.ICodigoOperacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodigoOperacionService implements ICodigoOperacionService {

    private final ISecuenciaCodigoRepository secuenciaRepo;
    private final ICodigoOperacionRepository codigoRepo;
    private final IUsuarioRepository usuarioRepo;

    @Override
    @Transactional
    public CodigoDTOResponse obtenerCodigo(Long usuarioId) {
        int anio = Year.now(ZoneId.of("America/Lima")).getValue();

        // 1. Bloqueo: serializa todas las solicitudes del año
        SecuenciaCodigo secuencia = secuenciaRepo.findByAnioForUpdate(anio)
                .orElseGet(() -> crearSecuencia(anio));

        // 2. Si ya tiene un código sin registrar, se devuelve el mismo
        Optional<CodigoOperacion> pendiente =
                codigoRepo.findByUsuarioIdAndEstado(usuarioId, EstadoReserva.PENDIENTE);
        if (pendiente.isPresent()) {
            return new CodigoDTOResponse(pendiente.get().getId(), pendiente.get().getCodigo());
        }

        // 3. Si no, se genera uno nuevo y se reserva
        String codigo = String.format("SRV-%d-%04d", anio, secuencia.siguiente());
        
        // 4. Buscar usuario por id
        Usuario user = buscarUsuarioPorId(usuarioId);

        CodigoOperacion reserva = new CodigoOperacion();
        reserva.setCodigo(codigo);
        reserva.setUsuario(user);
        codigoRepo.save(reserva);

        return new CodigoDTOResponse(reserva.getId(), reserva.getCodigo());
    }

    private Usuario buscarUsuarioPorId(Long usuarioId) {
        return usuarioRepo.findById(usuarioId).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Override
    @Transactional
    public void confirmarUso(Long usuarioId, Long id) {
        CodigoOperacion reserva = codigoRepo
                .findByUsuarioIdAndEstado(usuarioId, EstadoReserva.PENDIENTE)
                .filter(r -> r.getId().equals(id))
                .orElseThrow(() -> new IllegalStateException("El código no está reservado para este usuario"));
        reserva.setEstado(EstadoReserva.USADO);
    }
    private SecuenciaCodigo crearSecuencia(int anio) {
        try {
            secuenciaRepo.saveAndFlush(new SecuenciaCodigo(anio));
        } catch (DataIntegrityViolationException e) {
            // otro hilo la creó primero: se ignora y se vuelve a leer
        }
        return secuenciaRepo.findByAnioForUpdate(anio).orElseThrow();
    }
}
