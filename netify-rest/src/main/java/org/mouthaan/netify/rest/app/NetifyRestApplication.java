package org.mouthaan.netify.rest.app;

import org.mouthaan.netify.rest.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"org.mouthaan.netify.rest.controller"})
@Import({SwaggerConfig.class})
public class NetifyRestApplication {

	public static void main(String[] args) {
        SpringApplication.run(new Class<?>[]{NetifyRestApplication.class}, args);
	}
}
