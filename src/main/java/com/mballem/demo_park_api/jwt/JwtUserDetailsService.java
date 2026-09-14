package com.mballem.demo_park_api.jwt;

import com.mballem.demo_park_api.entity.Usuario;
import com.mballem.demo_park_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsername(username);
        return new JwtUserDetails(usuario);
    }

    public JwtToken getTokenAutheticated(String username){
        // Usuario.Role role = usuarioService.buscarRolePorUsername(username);
        Usuario usuario = usuarioService.buscarPorUsername(username);
        return JwtUtils.createToken(usuario.getId(),username,usuario.getRole().name().substring("Role_".length()));
    }
}
