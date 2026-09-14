package com.mballem.demo_park_api.web.dto.mapper;

import com.mballem.demo_park_api.web.dto.PageableDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableMapper<T> {

    @SuppressWarnings("unchecked")
    public static <T> PageableDTO<T> toDto(Page<T> page){
        return new ModelMapper().map(page,PageableDTO.class);
    }
}
