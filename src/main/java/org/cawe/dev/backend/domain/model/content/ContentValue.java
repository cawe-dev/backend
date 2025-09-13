package org.cawe.dev.backend.domain.model.content;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "contentType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextNodesContent.class, name = "TEXT"),
        @JsonSubTypes.Type(value = ImageContent.class, name = "IMAGE"),
        @JsonSubTypes.Type(value = VideoContent.class, name = "VIDEO")
})
public sealed interface ContentValue permits TextNodesContent, ImageContent, VideoContent {
}
