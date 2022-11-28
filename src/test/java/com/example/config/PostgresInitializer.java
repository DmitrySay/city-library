package com.example.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String IMAGE = "postgres:13";
    private static final String TEST_DATABASE = "TEST";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";

    private final PostgreSQLContainer postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse(IMAGE)
                    .asCompatibleSubstituteFor("postgres"))
                    .withReuse(true) // for all tests
                    .withDatabaseName(TEST_DATABASE)
                    .withUsername(USER_NAME)
                    .withPassword(PASSWORD)
                    .withFileSystemBind("src/main/resources/", "/tmp/");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        postgresContainer.start();
        TestPropertyValues.of(
                        "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
                        "spring.datasource.username=" + postgresContainer.getUsername(),
                        "spring.datasource.password=" + postgresContainer.getPassword(),
                        "spring.datasource.driverClassName=" + postgresContainer.getDriverClassName()
                )
                .applyTo(applicationContext.getEnvironment());
    }
}








