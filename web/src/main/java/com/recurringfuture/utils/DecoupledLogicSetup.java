package com.recurringfuture.utils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Slf4j
@Component
public class DecoupledLogicSetup {

    private final SpringResourceTemplateResolver springResourceTemplateResolver;

    public DecoupledLogicSetup(SpringResourceTemplateResolver springResourceTemplateResolver) {
        this.springResourceTemplateResolver = springResourceTemplateResolver;
    }

    @PostConstruct
    public void init(){
        springResourceTemplateResolver.setUseDecoupledLogic(true);
        log.info("Init DecoupledLogicSetup");
    }

}
