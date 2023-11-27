package com.proj.controller.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

/**** Thymeleaf Explainer ****
 * What is Thymeleaf? -> A View technology. A way to serve e.g. html to the frontend.
 * What is Thymeleaf's relation to Spring? -> https://stackoverflow.com/a/52660274 (JSP is similar to Thymeleaf)
 * 
 * (excerpt from https://stackoverflow.com/a/52660274):
 * - Spring provides the Spring MVC implementation for this.
 * - Spring MVC, implements the Model View Controller (MVC) application pattern.
 * - And JSP, Java Server Pages, is a View technology. That is the way the framework serves views towards the client's browser.
 * - So when asking what is the difference between, JSP and Spring. The question is a bit like asking what is the difference between a car and a wheel?
 * - Spring is the "frame the car is built-in" and the wheel is a specific part.
 * - In short: Both technologies are used in creating an application, Spring provides the flow, and JSP provides the way we represent our web page.
 */

/**
 * Configures Thymeleaf to look for html pages in our 'FrontEnd' folder, but can be configured to do much more!
 * <p>
 * Code based on https://codippa.com/change-thymeleaf-template-location-in-spring-boot/
 * <p>
 * @see https://www.thymeleaf.org/doc/tutorials/3.1/thymeleafspring.html
 * @see https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/viewresolver.html
 */
@Configuration
public class ThymeleafConfig {
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:../../../../FrontEnd/PublicResources"); // Custom template location
        templateResolver.setSuffix(".html"); // File type to search for
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        return resolver;
    }
}
