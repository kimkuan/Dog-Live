package com.ssafy.config;

import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration //application.yaml에서 어떤 properties를 읽을지 지정
@EnableTransactionManagement
@EnableJpaRepositories(                                                             //Jpa에 관한 설정 및 파일의 위치 명시
        entityManagerFactoryRef = "communityEntityManager",
        transactionManagerRef = "communityTransactionManager",
        basePackages = "com.ssafy.db.repository.community"                               //repository의 위치
)
public class CommunityDBConfig extends HikariConfig {

    @Bean
    @ConfigurationProperties("community.datasource")
    public DataSourceProperties communityDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("community.datasource.configuration")
    public DataSource communityDataSource(@Qualifier("communityDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean communityEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                           @Qualifier("communityDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.ssafy.db.entity.community")
                .persistenceUnit("communityEntityManager")
                .build();
    }

    @Bean
    public PlatformTransactionManager communityTransactionManager(@Qualifier("communityEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}