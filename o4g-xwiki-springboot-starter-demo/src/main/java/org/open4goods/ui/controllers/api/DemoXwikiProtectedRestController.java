package org.open4goods.ui.controllers.api;

import org.open4goods.ui.controllers.ui.DemoThymleafXwikiContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

/**
 * This controller allows informations and communications from fetchers
 * @author goulven
 *
 */
@RestController

public class DemoXwikiProtectedRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoXwikiProtectedRestController.class);

//	
//	@PatchMapping(path = "/feeds")
//	@Operation(summary="Manualy run the indexation of all feeds")
//	@PreAuthorize("hasAuthority('"+RolesConstants.ROLE_ADMIN+"')")
//	public IndexationResponse indexFeeds() {
//		feedService.fetchFeeds();
//		return new IndexationResponse();
//	}
	
	
	@GetMapping(path = "/allgroup")
	@PreAuthorize("hasAuthority('XWIKIALLGROUP')")
	public void testAuthorization() {
		LOGGER.info("AUTHORIZED");
	}
	

}
