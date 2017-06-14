package com.syndiceo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({
	"classpath:META-INF/spring.xml",
	  "classpath:META-INF/spring-taskconf.xml"
})
@EntityScan(basePackages ={ "com.syndiceo.model","com.syndiceo.proc.model","org.drools.persistence.info"})
public class SyndicRestApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SyndicRestApplication.class, args);
//		String[] beanNames = context.getBeanDefinitionNames();
//        Arrays.sort(beanNames);
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }
	}
}
