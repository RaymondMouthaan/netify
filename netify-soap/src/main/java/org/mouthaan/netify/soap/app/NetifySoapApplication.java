package org.mouthaan.netify.soap.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"org.mouthaan.netify.domain"})
@EntityScan(basePackages = {"org.mouthaan.netify.domain"})
@ComponentScan(basePackages = {
        "org.mouthaan.netify.soap",
        "org.mouthaan.netify.service",
        "org.mouthaan.netify.common",
        "org.mouthaan.netify.domain"})
//@Slf4j
public class NetifySoapApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(new Class<?>[]{NetifySoapApplication.class}, args);
    }

//    @Profile("local")
//    @Bean
//    public EmbeddedServletContainerFactory embeddedServletContainerFactoryLocal() {
//        return new TomcatEmbeddedServletContainerFactory() {
//
//            @Override
//            protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
//                    Tomcat tomcat) {
//                tomcat.enableNaming();
//                return super.getTomcatEmbeddedServletContainer(tomcat);
//            }
//
//            protected void postProcessContext(Context context) {
//                ContextResource resource = new ContextResource();
//                resource.setName("jdbc/tadta");
//                resource.setType(DataSource.class.getName());
//                resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
//                resource.setProperty("driverClassName", "org.h2.Driver");
//                resource.setProperty("url", "jdbc:h2:mem:test_mem");
//                resource.setProperty("username", "sa");
//                resource.setProperty("password", "");
//                context.getNamingResources().addResource(resource);
//            }
//        };
//    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
    }
}


