package com.mballem.demo_park_api.web.dto.mapper;

import com.mballem.demo_park_api.entity.Cliente;
import com.mballem.demo_park_api.web.dto.ClienteCreateDTO;
import com.mballem.demo_park_api.web.dto.ClienteResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDTO dto){
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDTO toDTO(Cliente cliente){
        return new ModelMapper().map(cliente, ClienteResponseDTO.class);
    }
}
