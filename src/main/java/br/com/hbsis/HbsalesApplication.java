package br.com.hbsis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class HbsalesApplication {

	public static void main(String[] args) {
		SpringApplication.run(HbsalesApplication.class, args);
	}

	/* CRIAR 'Bean Rest Template' PARA CONECTAR AUTOMATICAMENTE O OBJETO 'Rest Template' */
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}


