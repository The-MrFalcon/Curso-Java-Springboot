package com.mballem.demo_park_api.web.dto;

import com.mballem.demo_park_api.repository.projection.ClienteVagaProjection;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PageableClienteVagaDTO  extends  PageableDTO<ClienteVagaProjection> {

    @ArraySchema(schema = @Schema(implementation = ClienteVagaProjection.class))
    @Override
    public List<?> getContent() {
        return super.getContent();
    }
}
