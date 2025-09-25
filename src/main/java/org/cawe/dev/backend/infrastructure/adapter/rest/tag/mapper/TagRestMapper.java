package org.cawe.dev.backend.infrastructure.adapter.rest.tag.mapper;

import org.cawe.dev.backend.domain.model.Tag;
import org.cawe.dev.backend.infrastructure.adapter.rest.tag.dto.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagRestMapper {

    Tag toTag(RegisterTagRequest registerTagRequest);

    Tag toTag(UpdateTagRequest updateTagRequest);

    RegisterTagRequest toRegisterTagRequest(Tag tag);

    RegisterTagResponse toRegisterTagResponse(Tag tag);

    UpdateTagResponse toUpdateTagResponse(Tag tag);

    DetailsTagResponse toDetailsTagResponse(Tag tag);
}
