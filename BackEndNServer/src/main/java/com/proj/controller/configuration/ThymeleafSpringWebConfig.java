package com.proj.controller.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**** Thymeleaf Explainer ****
 * What is Thymeleaf?
 *  -> A View technology. A way to serve e.g. html to the frontend.
 * What is Thymeleaf's relation to Spring? 
 *  -> https://stackoverflow.com/a/52660274 (JSP is similar to Thymeleaf)
 *      - Spring provides the Spring MVC implementation for this.
 *      - Spring MVC, implements the Model View Controller (MVC) application pattern.
 *      - And JSP, Java Server Pages, is a View technology. That is the way the framework serves views towards the client's browser.
 *      - So when asking what is the difference between, JSP and Spring. The question is a bit like asking what is the difference between a car and a wheel?
 *      - Spring is the "frame the car is built-in" and the wheel is a specific part.
 *      - In short: Both technologies are used in creating an application, Spring provides the flow, and JSP provides the way we represent our web page.
 * 
 * *** From thymeleaf.org ***
 * Thymeleaf is a modern server-side Java template engine for both web and standalone environments, 
 * capable of processing HTML, XML, JavaScript, CSS and even plain text.
 * The main goal of Thymeleaf is to provide an elegant and highly-maintainable way of creating templates.
 * **************************
 */

@Configuration
public class ThymeleafSpringWebConfig implements WebMvcConfigurer {
    // Field
    @Autowired
    private ApplicationContext applicationContext;

    // Method
    /**
     * Resolves the path to the ressources specified, which can then be parsed by a template engine.
     * <p>
     * SpringResourceTemplateResolver automatically integrates with Spring's own
     * resource resolution infrastructure.
     * @return
     */
    @Bean
    public SpringResourceTemplateResolver templateResolver(){

        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("classpath:/FrontEnd/public/html/");
        templateResolver.setSuffix(".html"); // HTML is the default value, added here for the sake of clarity.
        templateResolver.setTemplateMode(TemplateMode.HTML);
        
        // Template cache is true by default. Set to false if you want
        // templates to be automatically updated when modified.
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    

    /**
     * The template engine used to render documents.
     * <p>
     * SpringTemplateEngine automatically applies SpringStandardDialect and
     * enables Spring's own MessageSource message resolution mechanisms.
     * @return
     */
    @Bean
    public SpringTemplateEngine templateEngine(){
        
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        
        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
        // speed up execution in most scenarios, but might be incompatible
        // with specific cases when expressions in one template are reused
        // across different data types, so this flag is "false" by default
        // for safer backwards compatibility.
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    /**
     * A ViewResolver responsible for rendering html and xhtml files.
     * Other file types can be specified in another ViewResolver.
     * <p>
     * ViewResolvers can be chained to render different files or special rendering of already configured files, e.g. "fish.html".
     * @return The ViewResolver which is to render the configured files.
     * @see https://www.thymeleaf.org/doc/tutorials/3.1/thymeleafspring.html#views-and-view-resolvers-in-spring-mvc
     * @see https://www.thymeleaf.org/doc/tutorials/3.1/thymeleafspring.html#views-and-view-resolvers-in-thymeleaf
     * @see https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/viewresolver.html
     */
    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());

        // Optional settings
        viewResolver.setOrder(0); // The order in which ViewResolvers are invoked.
        viewResolver.setViewNames(new String[] {".html", ".xhtml"});
        return viewResolver;
    }

    /**
     * Specifies the location of static ressources, e.g. CSS and JS.
     * @see https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/ResourceHandlerRegistry.html
     * @see https://zetcode.com/spring/resourcehandlerregistry/ 
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 'addResourceHandler' is the pattern to match when receiving a GET request on a resource.
        // E.g. if a CSS file in HTML is specified as: "/css/genericStyle.css" then a pattern of "css/**" will match that.
        // 'addResourceLocations' specifies the path to look for a resource if the pattern matches.

        registry.addResourceHandler("images/**").addResourceLocations("classpath:/FrontEnd/public/images/");
        registry.addResourceHandler("js/**").addResourceLocations("classpath:/FrontEnd/public/js/");
        registry.addResourceHandler("css/**").addResourceLocations("classpath:/FrontEnd/public/css/");
        //registry.addResourceHandler("favicon.ico").addResourceLocations("classpath:/FrontEnd/public/");
    }
}

