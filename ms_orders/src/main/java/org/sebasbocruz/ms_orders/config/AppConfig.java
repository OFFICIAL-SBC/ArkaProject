package org.sebasbocruz.ms_orders.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
      basePackages = {
              "org.sebasbocruz.ms_orders.application"
      },
    includeFilters = {
          @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
    }
)
public class AppConfig {

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }
}
