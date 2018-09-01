//package org.mouthaan.netify.domain.jpa.config;
//
//import org.hibernate.dialect.DB2400Dialect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.context.annotation.Profile;
//import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Profile("local")
//@Configuration
//@EnableTransactionManagement
//public class JpaConfigurationJtaDb2Local {
//
//    private final JpaVendorAdapter jpaVendorAdapter;
//
//    @Autowired
//    @Lazy
//    public JpaConfigurationJtaDb2Local(JpaVendorAdapter jpaVendorAdapter) {
//        this.jpaVendorAdapter = jpaVendorAdapter;
//    }
//
//    @Bean
//    public DataSource ntfyDataSource() {
//        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
//        return lookup.getDataSource("jdbc/ntfydta");
//    }
//
//    @Bean
//    public EntityManager entityManager() {
//        return entityManagerFactory().createEntityManager();
//    }
//
//    @Bean
//    public EntityManagerFactory entityManagerFactory() {
//        Properties properties = new Properties();
//        properties.put(org.hibernate.cfg.Environment.DIALECT, DB2400Dialect.class.getName());
//        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(ntfyDataSource());
//        emf.setJpaVendorAdapter(jpaVendorAdapter);
//        emf.setPackagesToScan("org.mouthaan.netify.domain.model");   // <- package for entities
//        emf.setPersistenceUnitName("netifyPU");
//        emf.setJpaProperties(properties);
//        emf.afterPropertiesSet();
//        return emf.getObject();
//    }
//
//    @Bean//(name = "jpaTransactionManager")
//    public PlatformTransactionManager transactionManager() {
//        return new JpaTransactionManager(entityManagerFactory());
//    }
//}
