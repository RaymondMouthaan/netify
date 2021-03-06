package org.mouthaan.netify.rest.app;

import org.mouthaan.netify.rest.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"org.mouthaan.netify.domain"})
@EntityScan(basePackages = {"org.mouthaan.netify.domain"})
@ComponentScan(basePackages = {
        "org.mouthaan.netify.rest",
        "org.mouthaan.netify.service",
        "org.mouthaan.netify.domain"})
@Import({SwaggerConfig.class})
public class NetifyRestApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
        SpringApplication.run(new Class<?>[]{NetifyRestApplication.class}, args);
	}

//	@Profile("local")
//    @Bean
//    public ServletWebServerFactory servletWebServerFactoryLocal() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                ContextResource resource = new ContextResource();
//                resource.setName("jdbc/ntfydta");
//                resource.setType(DataSource.class.getName());
//                resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
//                resource.setProperty("driverClassName", "org.h2.Driver");
//                resource.setProperty("url", "jdbc:h2:mem:test_mem");
//                resource.setProperty("username", "sa");
//                resource.setProperty("password", "");
//                context.getNamingResources().addResource(resource);
//            }
//        };
//        return tomcat;
//    }

//    @Profile("dev")
//    @Bean
//    public ServletWebServerFactory servletWebServerFactoryDev() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                ContextResource resource = new ContextResource();
//                resource.setName("jdbc/ntfydta");
//                resource.setType(DataSource.class.getName());
//                resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
//                resource.setProperty("driverClassName", "org.mariadb.jdbc.Driver");
//                resource.setProperty("url", "jdbc:mariadb://jakarta.indonesia:3406/testdb");
//                resource.setProperty("username", "root");
//                resource.setProperty("password", "my-secret-pw");
//                context.getNamingResources().addResource(resource);
//            }
//        };
//        return tomcat;
//    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
    }
}
