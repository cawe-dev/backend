package org.cawe.dev.backend.domain.model;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

public class Post extends BaseModel {
    private Integer id;

    @NotNull(message = "The title must be informed")
    private String title;

    @NotNull(message = "The author must be informed")
    private User author;

    @NotNull(message = "The contents must be informed")
    private List<Content> contents;

    @NotNull(message = "The categories must be informed")
    private Set<Category> categories;

    @NotNull(message = "The tags must be informed")
    private Set<Tag> tags;
}
