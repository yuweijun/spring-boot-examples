package com.example.spring.jdbc.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.util.Set;

/**
 * @author yuweijun 2017-03-14
 */
public class ComponentClassScanner  {

    public static void main(String[] args) {
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(true);
        String basePackage = "com/example";
        Set<BeanDefinition> components = provider.findCandidateComponents(basePackage);
        for (BeanDefinition component : components) {
            System.out.printf("Component: %s\n", component.getBeanClassName());
        }
    }
}
