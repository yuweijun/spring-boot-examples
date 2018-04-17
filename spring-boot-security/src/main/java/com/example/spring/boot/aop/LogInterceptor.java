package com.example.spring.boot.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * http://sishuok.com/forum/posts/list/281.html
 * AspectJ类型匹配的通配符：
 * *：匹配任何数量字符；
 * ..：匹配任何数量字符的重复，如在类型模式中匹配任何数量子包；而在方法参数模式中匹配任何数量参数。
 * +：匹配指定类型的子类型；仅能作为后缀放在类型模式后边。
 * <p>
 * http://www.tuicool.com/articles/QNny6r
 *
 * @Aspect 来定义一个切面
 * @Order 定义了该切面切入的顺序
 * @Pointcut 配置一个切点，基于切点表达式
 * <p>
 * before after around 定义不同类型的advice.
 */
@Aspect
@Component // for auto scan
@Order(2)
public class LogInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.example.service..*.getUserById(..))")
    public void pointCutMethod() {
    }

    @Before(value="execution(* com.example.service..*.getUserById(..)) && args(userId)", argNames="userId")
    public void printUserId(long userId) {
        logger.info("userId long value : {}" + userId);
    }

    @Before(value="execution(* com.example.service..*.getUserById(..)) && args(userId)", argNames="userId")
    public void printUserId(int userId) {
        // 参数名可以不一样，但类型必须是long的，如果类型不正确，不会被AOP拦截到
        // 这个方法并不会被运行，因为类型int不区配
        logger.info("userId int value : {}" + userId);
    }

    @Before(value="execution(* com.example.service..*.getUserById(*)) && args(id)", argNames="id")
    public void userId(long id) {
        // 参数名可以不一样，但类型必须是long的，如果类型不正确，不会被AOP拦截到
        logger.info("userId is {}" + id);
    }

    @Before("pointCutMethod()")
    public void before() {
        logger.info("method before ===============");
    }

    @After("pointCutMethod()")
    public void after() {
        logger.info("method after ===============");
    }

    @Before("pointCutMethod()")
    public void before2(JoinPoint jp) {
        Object[] args = jp.getArgs();
        logger.info("{}", args.length);
        logger.info("{}", jp.getTarget());
        logger.info("{}", jp.getThis());
        logger.info("{}", jp.getSignature());
        logger.info("method start2");
    }

    @Before("execution(public * com.example.service..*.findAll(..))")
    public void beforeFindAll() {
        logger.info("before find all users ===============");
    }

    @AfterReturning("execution(public * com.example.service..*.findAll(..))")
    public void afterReturning() {
        logger.info("method AfterReturning");
    }

    @AfterThrowing("execution(public * com.example.service..*.findAll(..))")
    // @Around("execution(public * com.example.service..*.*(..))")
    public void afterThrowing() {
        logger.info("method AfterThrowing");
    }

    @Around("execution(public * com.example.service..*.findAll(..))")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        logger.info("method Around");
        Object ret = jp.proceed();
        logger.info("{}", jp.getTarget());
        return ret;
    }

}