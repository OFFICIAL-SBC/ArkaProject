package org.sebasbocruz.ms_inventory.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = {
                "org.sebasbocruz.ms_inventory.commands.application",
                "org.sebasbocruz.ms_inventory.queries.application"
        },
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        }
)
public class AppConfig {
}
