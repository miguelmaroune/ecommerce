package com.ecommerce.config;

import com.ecommerce.entity.Country;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.ProductCategory;
import com.ecommerce.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration //So Spring will pick it up when we run
public class MyDataRestConfig implements RepositoryRestConfigurer
{
    @Value("${allowed.origins}")
    private String[ ] theAllowedOrigins ;
    private EntityManager entityManager;

    //autoWire Jpa entity manager .
    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager)
    {
        entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod [] theUnsupportedActions = {HttpMethod.DELETE,HttpMethod.PUT,HttpMethod.POST,
                                               HttpMethod.PATCH};
//        dissable HTTP methods for Product : Delete , Put , Post .
        disableHttpMethods(Product.class,config, theUnsupportedActions);

//        dissable HTTP methods for ProductCategory : Delete , Put , Post .
        disableHttpMethods(ProductCategory.class,config, theUnsupportedActions);

        disableHttpMethods(Country.class,config, theUnsupportedActions);

        disableHttpMethods(State.class,config, theUnsupportedActions);

        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
//    call internal helper method .
        exposeIds(config);

        //configure cors mapping
        String pathPattern = config.getBasePath()+"/**";
        System.out.println(pathPattern);
        cors.addMapping(pathPattern).allowedOrigins(theAllowedOrigins);

    }

    private void disableHttpMethods(Class theClass , RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config)
    {
//        expose entity ids

//        - get a list of all entity classes from the entity manager.
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities() ;

//        create an array of the entity types.
        List<Class> entityClasses = new ArrayList<>();
//        get the entity types for the entities.
        for (EntityType tempEntityType : entities)
        {
            entityClasses.add(tempEntityType.getJavaType());
        }
//        expose the entity ids for the array of entity/doamin types/
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);


    }
}
