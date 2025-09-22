package org.cawe.dev.backend.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.cawe.dev.backend.domain.enumeration.ContentTypeEnum;
import org.cawe.dev.backend.domain.model.content.ContentValue;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Content extends BaseModel {
    private Integer id;

    @NotNull(message = "The content order must be informed")
    private Short contentOrder;

    @NotNull(message = "The content type must be informed")
    private ContentTypeEnum type;

    @NotNull(message = "The value must be informed")
    private ContentValue value;
}
