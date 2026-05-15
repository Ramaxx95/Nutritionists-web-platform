package com.inutri.config;

import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class JpaConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = builder
                .dataSource(dataSource)
                .packages("com.inutri")
                .persistenceUnit("default")
                .build();

        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter() {
            @Override
            public String getDatabasePlatform() {
                return "org.hibernate.dialect.H2Dialect";
            }
        });

        return factory;
    }
}