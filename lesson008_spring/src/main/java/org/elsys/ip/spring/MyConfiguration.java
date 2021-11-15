package org.elsys.ip.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class MyConfiguration {
    @Bean
    public Logic getLogic() { return new Logic(); }
}
