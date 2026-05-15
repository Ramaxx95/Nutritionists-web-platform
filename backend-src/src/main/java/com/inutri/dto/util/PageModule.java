package com.inutri.dto.util;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.data.domain.Page;

import java.io.Serial;

/*
 * Creditos:
 * Mikhail Kopylov, Feb-17-2019
 * https://stackoverflow.com/questions/52490399/spring-boot-page-deserialization-pageimpl-no-constructor
 */

public class PageModule extends SimpleModule {

    @Serial
    private static final long serialVersionUID = 1L;

    public PageModule() {
        addDeserializer(Page.class, new PageDeserializer());
    }

}
