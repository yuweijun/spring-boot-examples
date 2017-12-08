package com.example.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html
 *
 * @author yuweijun 2016-09-06
 */
public class SpringElTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringElTest.class);

    @Test
    public void test1() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'.concat('!')");
        String message = (String) exp.getValue();
        LOGGER.info("message is : {}", message);
    }

    @Test
    public void test2() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("T(java.lang.Math).random() * 100.0");
        double value = (double) exp.getValue();
        LOGGER.info("value is : {}", value);

    }

    @Test
    public void test3() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("new String('hello world').toUpperCase()");
        String message = exp.getValue(String.class);
        LOGGER.info("message is : {}", message);
    }
}
