package org.cawe.dev.backend.application.port.driver.tag;

import org.cawe.dev.backend.domain.model.Tag;

public interface DeleteTagUseCase {

    void execute(Integer id);
}
