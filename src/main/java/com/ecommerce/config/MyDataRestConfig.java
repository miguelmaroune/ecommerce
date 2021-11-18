package com.ecommerce.config;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.ProductCategory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
@Configuration //So Spring will pick it up when we run
public class MyDataRestConfig implements RepositoryRestConfigurer
{
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod [] theUnsupportedActions = {HttpMethod.DELETE,HttpMethod.PUT,HttpMethod.POST};
//        dissable HTTP methods for Product : Delete , Put , Post .
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

//        dissable HTTP methods for ProductCategory : Delete , Put , Post .
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
    }
}
