package com.skb.xpg.nxpg.svc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.skb.xpg.nxpg.svc.NXPGSvcApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NXPGSvcApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("devtest")
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