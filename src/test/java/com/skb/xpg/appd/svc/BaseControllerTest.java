package com.skb.xpg.appd.svc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MasAppdXpgSvcApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("jenkins")
public class BaseControllerTest {
	
	@LocalServerPort
	private int port;
	
	@Test
	public void contextLoads() {
	}
	
	protected String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
	
}
