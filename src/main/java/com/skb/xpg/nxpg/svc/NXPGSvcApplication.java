package com.skb.xpg.nxpg.svc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.skb.xpg.nxpg.svc.config.CommonConfiguration;

/**
 * Created by Jay Lee on 21/12/2016.
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
@Import(CommonConfiguration.class) 
public class NXPGSvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(NXPGSvcApplication.class, args);
    }
    
}
