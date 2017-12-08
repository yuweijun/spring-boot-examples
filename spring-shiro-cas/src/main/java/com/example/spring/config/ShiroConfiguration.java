package com.example.spring.config;

import com.example.spring.realm.ShiroCasRealm;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroConfiguration.class);

    /**
     * <bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
     * <property name="securityManager" ref="securityManager"/>
     * <property name="loginUrl" value="https://localhost:8443/cas/login?service=http://localhost:8181/login/cas"/>
     * <property name="successUrl" value="/"/>
     * <property name="unauthorizedUrl" value="/test"/>
     * <property name="filters">
     * <util:map>
     * <entry key="cas" value-ref="casFilter"/>
     * </util:map>
     * </property>
     * <property name="filterChainDefinitions">
     * <value>
     * /login/cas = cas
     * /test = anon
     * / = user
     * </value>
     * </property>
     * </bean>
     *
     * @param securityManager
     * @param environment
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager, CasFilter casFilter, Environment environment) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("cas", casFilter);

        //拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");

        // cas: 所有url都必须cas认证通过才可以访问
        // /login/cas = cas
        filterChainDefinitionMap.put("/login/cas", "cas");

        // anon: 可以匿名访问的地址
        filterChainDefinitionMap.put("/test", "anon");

        // user访问的地址
        // 过滤链定义，从上向下顺序执行，一般将 /** 放在最为下边
        filterChainDefinitionMap.put("/**", "user");

        // 从cas重定向回来到cas的filter指定的url，处理ticket，从ticket中得到登陆用户的信息
        // 1. request URL: http://localhost:8181/
        // 2. redirect to: https://localhost:8443/cas/login?service=http://localhost:8181/login/cas
        // 3. redirect to: http://localhost:8181/login/cas?ticket=ST-223-c7DIBI4B9vwQKFbYsSVL-login.example.com
        // 4. redirect to:http://localhost:8181/
        String login = environment.getProperty("cas.service.login") + "?service=" + environment.getProperty("app.service.security");
        LOGGER.info("cas login url : {}", login);
        shiroFilterFactoryBean.setLoginUrl(login);

        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/");

        // 未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/test");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * <bean id="securityManager" class="org.apache.shiro.web.mgt.DefaultWebSecurityManager">
     * <property name="realm" ref="shiroCasRealm"/>
     * </bean>
     *
     * @param shiroCasRealm
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager(ShiroCasRealm shiroCasRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroCasRealm);
        return securityManager;
    }

    /**
     * <bean id="casFilter" class="org.apache.shiro.cas.CasFilter">
     * <property name="failureUrl" value="/test"/>
     * </bean>
     *
     * @return
     */
    @Bean
    public CasFilter casFilter() {
        CasFilter casFilter = new CasFilter();
        casFilter.setFailureUrl("/test");
        return casFilter;
    }

    @Bean
    public ShiroCasRealm shiroCasRealm(Environment environment) {
        ShiroCasRealm shiroCasRealm = new ShiroCasRealm();
        shiroCasRealm.setCasServerUrlPrefix(environment.getProperty("cas.url.prefix"));
        shiroCasRealm.setCasService(environment.getProperty("app.service.security"));
        return shiroCasRealm;
    }

    /**
     * 开启Shiro的注解功能(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证，
     * 需要配置两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)实现此功能。
     *
     * <bean class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">
     * <property name="securityManager" ref="securityManager"/>
     * </bean>
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * <bean id="cacheManager" class="org.apache.shiro.cache.MemoryConstrainedCacheManager"/>
     *
     * @return
     */
    @Bean
    public MemoryConstrainedCacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    /**
     * <bean id="lifecycleBeanPostProcessor" class="org.apache.shiro.spring.LifecycleBeanPostProcessor"/>
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}

