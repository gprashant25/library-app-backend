package com.luv2code.spring_boot_library.config;

import com.luv2code.spring_boot_library.entity.Book;
import com.luv2code.spring_boot_library.entity.Message;
import com.luv2code.spring_boot_library.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

// Here we're using the Spring Data REST Configuration which will automatically provide the @GetMapping http request and we can disable or block the other http methods like POST, DELETE etc. And instead of manually creating @RestController and manually defining methods for access: @GetMapping
// So here were only allowing GET http methods, which is the READ-ONLY option allowed for our Books in our application.

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    // Below URL is the React frontend library-app url. This will allow us to be able to properly make request to our frontend, and we'll stick this variable in some kind of cors mapping.
    private String theAllowedOrigins = "https://localhost:3000";

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
                                                     CorsRegistry cors){

        // creating an HttpMethod array. This we're doing bcos we want only the read only functionality (ie. only the http GET method)
        HttpMethod[] theUnsupportedActions = {
                HttpMethod.POST,
                HttpMethod.PATCH,
                HttpMethod.DELETE,
                HttpMethod.PUT
        };

        // here we want to expose all the IDs for these classes ie we're exposing the primary key while accessing GET http method. So, we're going to be using the primary key for functionality on the frontend so, we know exactly what book we're going to be using.
        config.exposeIdsFor(Book.class);
        config.exposeIdsFor(Review.class);
        config.exposeIdsFor(Message.class);

        disableHttpMethods(Book.class, config, theUnsupportedActions);
        disableHttpMethods(Review.class, config, theUnsupportedActions);
        disableHttpMethods(Message.class, config, theUnsupportedActions);

        // Configure CORS mapping. by adding CORS mapping the client ie frontend application can send http request to the backend server for communicating with the REST API
        cors.addMapping(config.getBasePath() + "/**")
                .allowedOrigins(theAllowedOrigins);
    }

    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config,
                                    HttpMethod[] theUnsupportedActions){

        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));


    }

}
