/**
 * TODO: For use with CSRF. Currently disabled.
 * We have disabled it as trying to implement it took longer than we had allocated time for.
 * As per our report, we did not prioritise security features highly, hence why we decided against spending more time to implement it.
 */



// package com.proj.controller.advisors;

// import org.apache.catalina.connector.Response;
// import org.springframework.http.HttpCookie;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.ResponseCookie;
// import org.springframework.http.ResponseEntity;
// import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
// import org.springframework.security.web.csrf.CsrfToken;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ModelAttribute;

// import jakarta.servlet.http.Cookie;
// import jakarta.servlet.http.HttpServletResponse;


// /** TODO: Fix SameSite attribute.
//  * Cookie “XSRF-TOKEN” does not have a proper “SameSite” attribute value. 
//  * Soon, cookies without the “SameSite” attribute or with an invalid value will be treated as “Lax”. 
//  * This means that the cookie will no longer be sent in third-party contexts. 
//  * If your application depends on this cookie being available in such contexts, please add the “SameSite=None“ attribute to it. 
//  * To know more about the “SameSite“ attribute, read https://developer.mozilla.org/docs/Web/HTTP/Headers/Set-Cookie/SameSite
//  * 
//  * Links that *might* help fix:
//  * https://stackoverflow.com/a/54507027 (Spring API Cookies)
//  * https://stackoverflow.com/a/43530185 (Spring Security Filters (using Jakarta Servlet API))
//  * https://stackoverflow.com/a/43106260 (Jakarta Servlet API)
//  * https://docs.spring.io/spring-security/reference/features/exploits/csrf.html#csrf-protection-ssa (Spring Session Framework)
//  * 
//  * Vauge idea of how to fix:
//  * https://docs.spring.io/spring-security/reference/features/exploits/csrf.html#csrf-considerations-timeouts
//  */


// @ControllerAdvice
// public class CsrfAdvisor {
	
//     /**
//      * Include a CSRF token in the header of every endpoint this ControllerAdvice applies to (currently all).
//      * <p>
//      * This way of doing it decreases performance. To improve, see: https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#deferred-csrf-token
//      * @param response The server's response to the client.
//      * @param csrfToken The CSRF token to include in the header.
//      * @see https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-other
//      */
//     @ModelAttribute
// 	public void getCsrfToken(HttpServletResponse response, CsrfToken csrfToken) {
//         String key = csrfToken.getHeaderName();
//         String value = csrfToken.getToken();
//         String options = "SameSite=strict";

//     //     System.out.println(key);
//     //     System.out.println(value);
//     //     System.out.println("\n\n");

//     //     // Format: "key=value; options"
//         String fullConcat = String.format("%s=%s; %s", key, value, options);
//         // String valueConcat = String.format("%s; %s", value, options);

// 		//response.addHeader("Set-Cookie", fullConcat);
//         response.setHeader(key, value);
// 	}
// }
