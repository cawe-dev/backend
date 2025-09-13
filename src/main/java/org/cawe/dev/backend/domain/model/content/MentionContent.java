package org.cawe.dev.backend.domain.model.content;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class MentionContent implements TextNode {
    UUID markerId;
    String value;
}
