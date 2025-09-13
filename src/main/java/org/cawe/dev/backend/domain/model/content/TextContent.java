package org.cawe.dev.backend.domain.model.content;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TextContent implements TextNode {
    String value;
}
