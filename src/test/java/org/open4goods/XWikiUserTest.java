package org.open4goods;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.open4goods.xwiki.XWikiServiceConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

public class XWikiUserTest {

	private  final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(XWikiServiceConfiguration.class, RestClientAutoConfiguration.class));
	
	
//	@Test
//	void shouldContainRestClientBean() {
//		this.contextRunner.run(context -> { 
//			assertTrue(context.containsBean("restClient"));
//		});
//	}
//	
//	@Test
//	void shouldContainXwikiClientBean() {
//		this.contextRunner.run(context -> { 
//			assertTrue(context.containsBean("xwikiClient"));
//		});
//	}
	
}
