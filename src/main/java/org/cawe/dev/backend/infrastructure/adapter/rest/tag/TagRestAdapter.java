package org.cawe.dev.backend.infrastructure.adapter.rest.tag;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cawe.dev.backend.application.port.driver.tag.*;
import org.cawe.dev.backend.domain.model.Tag;
import org.cawe.dev.backend.infrastructure.adapter.persistence.entity.TagEntity;
import org.cawe.dev.backend.infrastructure.adapter.rest.tag.dto.*;
import org.cawe.dev.backend.infrastructure.adapter.rest.tag.mapper.TagRestMapper;
import org.cawe.dev.backend.util.HeaderUtil;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.cawe.dev.backend.util.EntityUtils.getBaseNameFromEntity;

@Slf4j
@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagRestAdapter {

    private final FindTagUseCase findTagUseCase;
    private final FindAllTagsUseCase findAllTagsUseCase;
    private final CreateTagUseCase createTagUseCase;
    private final UpdateTagUseCase updateTagUseCase;
    private final DeleteTagUseCase deleteTagUseCase;
    private final TagRestMapper tagRestMapper;

    @Value("${app.name}")
    private String applicationName;

    public static final String ENTITY_NAME = getBaseNameFromEntity(TagEntity.class.getName());

    @GetMapping
    @Operation(summary = "Search for all tags")
    public ResponseEntity<Page<DetailsTagResponse>> getTags(@ParameterObject final Pageable pageable) {
        log.debug("REST request to get all Tags: {}", pageable);
        Page<Tag> tags = this.findAllTagsUseCase.findAll(pageable);
        Page<DetailsTagResponse> response = tags.map(this.tagRestMapper::toDetailsTagResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Search for a tag by id")
    public ResponseEntity<DetailsTagResponse> getTag(@PathVariable @Valid final Integer id) {
        log.debug("REST request to get tag with id: {}", id);
        Tag tag = this.findTagUseCase.execute(id);
        DetailsTagResponse response = this.tagRestMapper.toDetailsTagResponse(tag);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a tag")
    public ResponseEntity<RegisterTagResponse> createTag(@RequestBody @Valid final RegisterTagRequest registerTagRequest) throws URISyntaxException {
        log.debug("REST request to save Tag : {}", registerTagRequest);
        Tag tag = this.tagRestMapper.toTag(registerTagRequest);
        tag = this.createTagUseCase.execute(tag);

        RegisterTagResponse response = this.tagRestMapper.toRegisterTagResponse(tag);

        return ResponseEntity
                .created(new URI("/api/v1/tags/" + response.id()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, response.id().toString()))
                .body(response);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update a tag")
    public ResponseEntity<UpdateTagResponse> updateTag(@PathVariable @Valid final Integer id,
                                                       @RequestBody @Valid final UpdateTagRequest updateTagRequest) {
        log.debug("REST request to update Tag : {}", updateTagRequest);
        Tag tag = this.tagRestMapper.toTag(updateTagRequest);
        tag = this.updateTagUseCase.execute(id, tag);

        UpdateTagResponse response = this.tagRestMapper.toUpdateTagResponse(tag);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, response.id().toString()))
                .body(response);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a tag")
    public ResponseEntity<Object> deleteTag(@PathVariable @Valid final Integer id) {
        this.deleteTagUseCase.execute(id);

        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, String.valueOf(id))).build();
    }
}
