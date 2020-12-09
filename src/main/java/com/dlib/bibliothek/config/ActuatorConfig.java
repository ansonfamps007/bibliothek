
package com.dlib.bibliothek.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/actuator-config.properties")
public class ActuatorConfig {
}
