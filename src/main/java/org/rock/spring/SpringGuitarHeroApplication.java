package org.rock.spring;

import io.smallrye.stork.Stork;
import io.smallrye.stork.api.ServiceDefinition;
import io.smallrye.stork.servicediscovery.consul.ConsulConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class SpringGuitarHeroApplication {

	private static final Logger logger = LoggerFactory.getLogger(SpringGuitarHeroApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringGuitarHeroApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	@Bean
	public Stork stork(){
		Stork.initialize();
		Stork stork = Stork.getInstance();
		stork.defineIfAbsent("guitar-hero-service", ServiceDefinition.of(new ConsulConfiguration()));
		return stork;
	}
}
