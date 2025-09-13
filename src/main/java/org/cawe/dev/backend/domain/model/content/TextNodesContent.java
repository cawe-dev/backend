package org.cawe.dev.backend.domain.model.content;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TextNodesContent implements ContentValue {
    List<TextNode> nodes;
}
