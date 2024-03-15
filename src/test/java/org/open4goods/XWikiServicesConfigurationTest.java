package org.open4goods;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.open4goods.xwiki.XWikiServiceConfiguration;
import org.open4goods.xwiki.config.XWikiServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)  // to fetch spring context
@EnableConfigurationProperties(value = XWikiServiceProperties.class)
@TestPropertySource("classpath:application-test.properties")
public class XWikiServicesConfigurationTest {

	// 'RestTemplateAutoConfiguration' needed to access 'RestTemplateBuilder' bean
	// 
	private  final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(XWikiServiceConfiguration.class, RestTemplateAutoConfiguration.class));
			// .withUserConfiguration(XWikiServiceProperties.class);
	
	@Autowired
	private XWikiServiceProperties xwikiProperties;
	
	
	@Test
	void testServicesPropertyFileBinded() {
		assertEquals(true, xwikiProperties.isHttpsOnly());
		
	}
	
	@Test
	void shouldContainRestTemplateBean() {
		this.contextRunner.run(
				context -> { assertTrue(context.containsBean("restTemplate"));});
	}
	
	@Test
	void shouldContainWebTemplateBean() {
		this.contextRunner.run(
				context -> { assertTrue(context.containsBean("webTemplate"));
		});
	}

	@Test
	void shouldContainRestServiceBean() {
		this.contextRunner.run(
				context -> { assertTrue(context.containsBean("xwikiRestService"));
		});
	}
		
	
	
//	
//	@Test
//	void shouldContainWebTemplate() {
//		this.contextRunner.run(context -> { 
//			assertTrue(context.containsBean("webTemplate"));
//		});
//	}
	
}
