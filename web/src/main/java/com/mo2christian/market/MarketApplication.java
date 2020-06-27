package com.mo2christian.market;

import com.mo2christian.market.common.SecurityFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.gcp.data.datastore.repository.config.EnableDatastoreRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableDatastoreRepositories
@EnableWebMvc
@SpringBootApplication
public class MarketApplication {


	public static void main(String[] args) {
		SpringApplication.run(MarketApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean securityFilter(@Value("${app.api-key}") String apiKey){
		FilterRegistrationBean registration = new FilterRegistrationBean<SecurityFilter>();
		registration.setFilter(new SecurityFilter(apiKey));
		registration.addUrlPatterns("/rest/*");
		return registration;
	}

}
