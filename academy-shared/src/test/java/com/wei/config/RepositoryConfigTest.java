package com.wei.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

// Import RepositoryConfig to inherit beans
// override @PropertySource
@Configuration
@Import(RepositoryConfig.class)
@PropertySource("classpath:application_test.properties")
public class RepositoryConfigTest {
}
