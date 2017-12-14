package com.example.spring.jsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * Created by yuweijun on 2017-12-14.
 */
public class EnableAutoConfigurationImportExample implements DeferredImportSelector, BeanClassLoaderAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnableAutoConfigurationImportExample.class);

    private ClassLoader classLoader;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Select and return the names of which class(es) should be imported
     * based on the AnnotationMetadata of the importing Configuration class.
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> beanNames = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, classLoader);
        for (String beanName : beanNames) {
            LOGGER.info(beanName);
        }
        return beanNames.toArray(new String[beanNames.size()]);
    }

}
