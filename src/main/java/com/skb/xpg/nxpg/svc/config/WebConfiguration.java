package com.skb.xpg.nxpg.svc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    
    @Autowired
    CertificationInterceptor certificationInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(certificationInterceptor)
                .addPathPatterns("/**");
    }
    
//    @Override
//    public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
//        configurer.setDefaultTimeout(2000);
//        configurer.registerCallableInterceptors(timeoutInterceptor());
//    }
//    
//    @Bean
//    public TimeoutCallableProcessingInterceptor timeoutInterceptor() {
//        return new TimeoutCallableProcessingInterceptor();
//    }
}
