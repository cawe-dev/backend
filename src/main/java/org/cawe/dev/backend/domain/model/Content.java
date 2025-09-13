package org.cawe.dev.backend.domain.model;

import jakarta.validation.constraints.NotNull;
import org.cawe.dev.backend.domain.enumeration.ContentTypeEnum;
import org.cawe.dev.backend.domain.model.content.ContentValue;

public class Content extends BaseModel {
    private Integer id;

    @NotNull(message = "The content order must be informed")
    private Short contentOrder;

    @NotNull(message = "The content type must be informed")
    private ContentTypeEnum type;

    @NotNull(message = "The content object must be informed")
    private ContentValue value;
}
