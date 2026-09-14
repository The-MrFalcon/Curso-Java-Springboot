package com.mballem.demo_park_api.web.dto;

import com.mballem.demo_park_api.repository.projection.ClienteProjection;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PageableClienteDTO extends PageableDTO<ClienteProjection> {
    @ArraySchema(schema = @Schema(implementation = ClienteProjection.class))
    @Override
    public List<?> getContent() {
        return super.getContent();
    }
}
