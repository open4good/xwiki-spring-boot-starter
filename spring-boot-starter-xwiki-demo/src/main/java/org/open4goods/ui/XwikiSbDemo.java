
package org.open4goods.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication()
public class XwikiSbDemo {

	private static final Logger logger = LoggerFactory.getLogger(XwikiSbDemo.class);
	public static void main(final String[] args) {
		SpringApplication.run(XwikiSbDemo.class, args);
	}


}
