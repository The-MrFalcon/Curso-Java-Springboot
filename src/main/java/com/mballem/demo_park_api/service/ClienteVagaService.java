package com.mballem.demo_park_api.service;

import com.mballem.demo_park_api.entity.ClienteVaga;
import com.mballem.demo_park_api.exception.EntityNotFoundException;
import com.mballem.demo_park_api.jwt.JwtUserDetails;
import com.mballem.demo_park_api.repository.ClienteVagaRepository;
import com.mballem.demo_park_api.repository.projection.ClienteVagaProjection;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteVagaService {

    private final ClienteVagaRepository repository;

    @Transactional
    public ClienteVaga salvar(ClienteVaga clienteVaga){
        return repository.save(clienteVaga);
    }

    @Transactional(readOnly = true)
    public ClienteVaga buscarPorRecibo(String recibo) {
        ClienteVaga cv = repository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
                () -> new EntityNotFoundException(String.format("Recibo '%s' não encontrado no sistema no check-out já realizado",recibo))
        );

        JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isAdmin = jwtUserDetails.getRole().equals("ROLE_ADMIN");

        boolean isClienteDonoRecibo = cv.getCliente().getUsuario().getUsername().equals(jwtUserDetails.getUsername());

        if (!isAdmin && !isClienteDonoRecibo) {
            throw new AccessDeniedException("Acesso negado, este recibo não te pertence");
        }
        return cv;
    }

    @Transactional(readOnly = true)
    public long getTotalDeVezesEstacionamentoCompleto(String cpf) {
        return repository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> buscarTodosPorClienteCpf(String cpf, Pageable pageable) {
        return repository.findAllByClienteCpf(cpf,pageable);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> buscarTodosPorUsuarioId(Long id, Pageable pageable) {
        return repository.findAllByClienteUsuarioId(id,pageable);
    }
}
