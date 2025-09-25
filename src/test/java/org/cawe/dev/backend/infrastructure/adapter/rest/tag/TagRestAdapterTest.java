package org.cawe.dev.backend.infrastructure.adapter.rest.tag;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cawe.dev.backend.AbstractIntegrationTest;
import org.cawe.dev.backend.TestFixtureUtil;
import org.cawe.dev.backend.application.port.driver.tag.*;
import org.cawe.dev.backend.domain.exception.EntityNotFoundException;
import org.cawe.dev.backend.domain.exception.TagNameAlreadyUsedException;
import org.cawe.dev.backend.domain.model.Tag;
import org.cawe.dev.backend.infrastructure.adapter.cognito.CognitoIdentityAdapter;
import org.cawe.dev.backend.infrastructure.adapter.rest.tag.dto.RegisterTagRequest;
import org.cawe.dev.backend.infrastructure.adapter.rest.tag.dto.UpdateTagRequest;
import org.cawe.dev.backend.infrastructure.security.JwtGrantedAuthoritiesExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class TagRestAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestFixtureUtil testFixtureUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateTagUseCase createTagUseCase;

    @MockitoBean
    private FindTagUseCase findTagUseCase;

    @MockitoBean
    private UpdateTagUseCase updateTagUseCase;

    @MockitoBean
    private DeleteTagUseCase deleteTagUseCase;

    @MockitoBean
    private FindAllTagsUseCase findAllTagsUseCase;

    @MockitoBean
    private CognitoIdentityAdapter cognitoIdentityAdapter;

    @MockitoBean
    private JwtGrantedAuthoritiesExtractor jwtGrantedAuthoritiesExtractor;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    public static final String BASE_URL = "/api/v1/tags";
    private static final String JSON_PATH_ID = "$.id";
    private static final String JSON_PATH_NAME = "$.name";
    private static final String ENTITY_NAME = "Tag";
    private static final int NON_EXISTENT_TAG_ID = 999;

    @Test
    @WithMockUser
    @DisplayName("It should be able to create a new tag successfully")
    void testCreateTagSuccessfully() throws Exception {
        Tag tag = testFixtureUtil.createTag();
        RegisterTagRequest requestBody = testFixtureUtil.createRegisterTagRequest(tag);

        when(createTagUseCase.execute(any(Tag.class))).thenReturn(tag);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", BASE_URL.concat("/" + tag.getId())))
                .andExpect(jsonPath(JSON_PATH_ID, is(tag.getId())))
                .andExpect(jsonPath(JSON_PATH_NAME, is(tag.getName())));

        verify(createTagUseCase, times(1)).execute(any(Tag.class));
    }

    @Test
    @WithMockUser
    @DisplayName("It should return 400 (Bad Request) when trying to create tag with blank name")
    void testReturnBadRequestWhenCreatingTagWithBlankName() throws Exception {
        RegisterTagRequest invalidRequestBody = new RegisterTagRequest("");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("It should return 409 (Conflict) when tag name already exists")
    void testCreateTagWithExistingName() throws Exception {
        Tag tag = testFixtureUtil.createTag();
        RegisterTagRequest requestBody = testFixtureUtil.createRegisterTagRequest(tag);

        when(createTagUseCase.execute(any(Tag.class))).thenThrow(new TagNameAlreadyUsedException());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isConflict());

        verify(createTagUseCase, times(1)).execute(any(Tag.class));
    }

    @Test
    @WithMockUser
    @DisplayName("It should find and return a tag by ID")
    void testFindTagById() throws Exception {
        Tag tag = testFixtureUtil.createTag();

        when(findTagUseCase.execute(tag.getId())).thenReturn(tag);

        mockMvc.perform(get(BASE_URL + "/" + tag.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ID, is(tag.getId())))
                .andExpect(jsonPath(JSON_PATH_NAME, is(tag.getName())));

        verify(findTagUseCase, times(1)).execute(tag.getId());
    }

    @Test
    @WithMockUser
    @DisplayName("It should return 404 (Not Found) when fetching a non-existent tag")
    void testReturnNotFoundWhenFindingNonExistentUser() throws Exception {
        when(findTagUseCase.execute(NON_EXISTENT_TAG_ID)).thenThrow(new EntityNotFoundException(ENTITY_NAME));

        mockMvc.perform(get(BASE_URL + "/" + NON_EXISTENT_TAG_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(findTagUseCase, times(1)).execute(NON_EXISTENT_TAG_ID);
    }

    @Test
    @WithMockUser
    @DisplayName("It should find all tags successfully")
    void testFindAllTagsSuccessfully() throws Exception {
        Tag tag = testFixtureUtil.createTag();
        Page<Tag> page = new PageImpl<>(List.of(tag));
        when(findAllTagsUseCase.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(tag.getId())))
                .andExpect(jsonPath("$.content[0].name", is(tag.getName())));

        verify(findAllTagsUseCase, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @WithMockUser
    @DisplayName("It should return an empty page when no tags exist")
    void testFindAllTagsWhenEmpty() throws Exception {
        Page<Tag> emptyPage = new PageImpl<>(Collections.emptyList());
        when(findAllTagsUseCase.findAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));

        verify(findAllTagsUseCase, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @WithMockUser
    @DisplayName("It should update a tag successfully")
    void testUpdateTagSuccessfully() throws Exception {
        Tag existingTag = testFixtureUtil.createTag();
        UpdateTagRequest updateRequest = new UpdateTagRequest("Updated Name");

        Tag updatedTag = new Tag(existingTag.getId(), updateRequest.name());

        when(updateTagUseCase.execute(any(Integer.class), any(Tag.class))).thenReturn(updatedTag);

        mockMvc.perform(put(BASE_URL + "/" + existingTag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ID, is(updatedTag.getId())))
                .andExpect(jsonPath(JSON_PATH_NAME, is(updatedTag.getName())));

        verify(updateTagUseCase, times(1)).execute(any(Integer.class), any(Tag.class));
    }

    @Test
    @WithMockUser
    @DisplayName("It should return 404 (Not Found) when trying to update a non-existent tag")
    void testReturnNotFoundWhenUpdatingNonExistentTag() throws Exception {
        UpdateTagRequest updateRequest = new UpdateTagRequest("Any Name");

        when(updateTagUseCase.execute(any(Integer.class), any(Tag.class))).thenThrow(new EntityNotFoundException(ENTITY_NAME));

        mockMvc.perform(put(BASE_URL + "/" + NON_EXISTENT_TAG_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(updateTagUseCase, times(1)).execute(any(Integer.class), any(Tag.class));
    }

    @Test
    @WithMockUser
    @DisplayName("It should delete a tag successfully")
    void testDeleteTagSuccessfully() throws Exception {
        int tagId = 1;
        doNothing().when(deleteTagUseCase).execute(tagId);

        mockMvc.perform(delete(BASE_URL + "/" + tagId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(deleteTagUseCase, times(1)).execute(tagId);
    }

    @Test
    @WithMockUser
    @DisplayName("It should return 404 (Not Found) when trying to delete a non-existent tag")
    void testReturnNotFoundWhenDeletingNonExistentTag() throws Exception {
        doThrow(new EntityNotFoundException(ENTITY_NAME)).when(deleteTagUseCase).execute(NON_EXISTENT_TAG_ID);

        mockMvc.perform(delete(BASE_URL + "/" + NON_EXISTENT_TAG_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(deleteTagUseCase, times(1)).execute(NON_EXISTENT_TAG_ID);
    }
}
