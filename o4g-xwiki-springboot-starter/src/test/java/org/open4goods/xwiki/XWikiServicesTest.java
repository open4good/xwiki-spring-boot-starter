package org.open4goods.xwiki;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.open4goods.xwiki.services.XWikiReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = XWikiServiceConfiguration.class)
@AutoConfigureWebClient
public class XWikiServicesTest {

	@Autowired XWikiReadService xwikiReadService; 
	
	@Test
	void testEmpty() {
		
		
		//xwikiReadService.downloadAttachment("sss");
		
		
		assertEquals(true, true);
		
	}
}
