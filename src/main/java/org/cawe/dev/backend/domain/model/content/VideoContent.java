package org.cawe.dev.backend.domain.model.content;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VideoContent implements ContentValue {
    String url;
    String caption;
}
