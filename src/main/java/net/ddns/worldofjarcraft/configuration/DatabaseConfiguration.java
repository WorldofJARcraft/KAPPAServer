package net.ddns.worldofjarcraft.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EntityScan("net.ddns.worldofjarcraft.DatabaseRepresentation")
@EnableJpaRepositories(basePackages = "net.ddns.worldofjarcraft.DatabaseRepresentation", entityManagerFactoryRef = "entityManagerFactory")
@PropertySource(value = "classpath:application.properties")
public class DatabaseConfiguration {
    @Value("${spring.datasource.url}") String url;
    @Value("${spring.datasource.username}") String user;
    @Value("${spring.datasource.password}") String pw;
    @Bean(name="dataSource")
    public DataSource getDataSource(){
        return DataSourceBuilder.create()
                .username(user)
                .password(pw)
                .url(url)
                .build();
    }
    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource){
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        return initializer;

    }
    @Bean
    public EntityManagerFactory entityManagerFactory(){
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("net.ddns.worldofjarcraft.DatabaseRepresentation");
        factory.setDataSource(getDataSource());
        factory.afterPropertiesSet();

        return factory.getObject();
    }
}
