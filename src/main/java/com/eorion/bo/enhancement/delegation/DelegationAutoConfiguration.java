package com.eorion.bo.enhancement.delegation;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan
public class DelegationAutoConfiguration {
    @Bean
    public GroupedOpenApi delegationGroup() {
        return GroupedOpenApi.builder()
                .group("delegation")
                .packagesToScan("com.eorion.bo.enhancement.delegation.adapter.inbound")
                .pathsToMatch("/enhancement/delegation/**")
                .build();
    }
}
