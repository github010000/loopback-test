package com.skb.xpg.nxpg.svc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles( profiles={"devtest"} )
public class NXPGSvcApplicationTests {

	@Test
	public void contextLoads() {
	}

}
