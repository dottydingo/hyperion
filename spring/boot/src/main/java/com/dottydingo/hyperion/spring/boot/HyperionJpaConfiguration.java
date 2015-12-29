package com.dottydingo.hyperion.spring.boot;

import com.dottydingo.hyperion.core.configuration.HyperionEndpointConfiguration;
import com.dottydingo.service.endpoint.EndpointHandler;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

import javax.servlet.Servlet;

/**
 */
@Configuration
@ImportResource({
        "classpath:com/dottydingo/hyperion/core/spring/spring-hyperion-data.xml",
        "classpath:com/dottydingo/hyperion/jpa/spring/spring-hyperion-data-jpa.xml",
        "classpath:com/dottydingo/hyperion/core/spring/spring-hyperion-endpoint.xml",
        "classpath:com/dottydingo/hyperion/core/spring/spring-hyperion-phase.xml",
        "classpath:com/dottydingo/hyperion/core/spring/spring-hyperion-pipeline-asynchronous.xml",
        "classpath:com/dottydingo/hyperion/core/spring/spring-hyperion-management.xml",
})
public class HyperionJpaConfiguration
{
    @Bean
    public ServletRegistrationBean hyperionServlet(EndpointHandler hyperionEndpointHandler,
                                                   HyperionEndpointConfiguration configuration)
    {
        Servlet servlet = new HyperionEndpointServlet(hyperionEndpointHandler);
        return new ServletRegistrationBean(servlet,configuration.getEndpointPath());
    }
}
