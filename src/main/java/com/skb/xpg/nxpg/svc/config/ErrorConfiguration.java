package com.skb.xpg.nxpg.svc.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration 
public class ErrorConfiguration extends ServerProperties { 
	
	@Override 
	public void customize(ConfigurableEmbeddedServletContainer container) { 
		super.customize(container); 
		container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/default")); 
		container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error/default")); 
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/default")); 
		container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/default")); 
		container.addErrorPages(new ErrorPage(HttpStatus.BAD_GATEWAY, "/error/default"));
		container.addErrorPages(new ErrorPage(HttpStatus.SERVICE_UNAVAILABLE, "/error/default"));
		container.addErrorPages(new ErrorPage("/error/default"));
	}
}