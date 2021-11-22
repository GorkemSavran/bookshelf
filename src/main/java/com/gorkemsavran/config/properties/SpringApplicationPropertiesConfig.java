package com.gorkemsavran.config.properties;

import com.gorkemsavran.config.CreateAdminOnStartUp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class SpringApplicationPropertiesConfig {
}
