package com.example.springedatajpainflearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@SpringBootApplication
public class SpringeDataJpaInflearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringeDataJpaInflearnApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(UUID.randomUUID().toString());
    }

    // OffsetDateTime, ZonedDateTime issue
    // refer: https://stackoverflow.com/questions/49170180/createdby-and-lastmodifieddate-are-no-longer-working-with-zoneddatetime
    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
